/**
 * Bismillah Hirrahman Nirrahim
 */

package com.toptal.controller;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.toptal.model.Jog;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import com.toptal.JogRepo;

import java.net.URLEncoder;
import java.time.ZoneOffset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

@RestController
public class JogController {

	@Autowired
	JogRepo repo;
	
	
	@RequestMapping(value = "/jogs/query/{query}", params = { "page", "size" })
	@ResponseBody
	public Page<Jog> findByQuery(@PathVariable("query") String query, Pageable p,
			@RequestParam("page") int page, 
			  @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
			  HttpServletResponse response) {
		
		Deque<String> brackets = new ArrayDeque<>();
		Deque<List<Jog>> queryResults = new ArrayDeque<>();
		Deque<String> operands = new ArrayDeque<>();
		List<Jog> combinedResults = Collections.emptyList();
		for (int i = 0; i<query.length(); i++) {
			
			while (query.charAt(i)=='(') {
				brackets.push("(");
				i++;
				//System.out.println(i);
			}
			
			// start of new query
			StringBuilder sb = new StringBuilder();
			while (query.charAt(i)!=')') {
				sb.append(query.charAt(i));
				i++;
			}
			
			
			// execute and store the query result
			System.out.println("Query: "+sb.toString());
			List<Jog> result = executeQuery(sb.toString(), p);
			
			if (result==null) { 
				// this means that there was some invalid operation inserted
				// and result turned out to be null. So we will terminate
				System.out.println("Error. Some invalid operation was inserted.");
				return new PageImpl<>(Collections.emptyList());
			}
			if (!result.isEmpty()) {
				System.out.println("No Problem?");
				queryResults.push(result);
			}

			brackets.pop();
			// if we are sitting on top of an Operand token, which is why there is null result, so don't push.. otherwish push
			if (result.isEmpty() && !operands.isEmpty() && i!=query.length()-1 && !brackets.isEmpty()) {
				queryResults.push(result);
			} else if (result.isEmpty() && query.charAt(i-1)!=')' && query.charAt(i-1)!=' ') {
				System.out.println("Current char: "+query.charAt(i));
				//System.out.println("Next char: "+query.charAt(i+1));
				// null result, and there is more to go?
				queryResults.push(result); 
			}
			System.out.println("Query line 0: "+queryResults.toString());
			
			
			if (!operands.isEmpty() && brackets.isEmpty()) {
				System.out.println("Performing in bracket operation now.");
				combinedResults = combineProcess(combinedResults, operands, queryResults);
				System.out.println("Query line 1: "+queryResults.toString());
			}
			
			// find operand token next
			System.out.println("AND Index: "+query.indexOf("AND", i));
			System.out.println("OR Index: "+query.indexOf("OR", i));
			System.out.println("Current char: "+query.charAt(i));
			System.out.println("Previous char: "+query.charAt(i-1));
			System.out.println("i : "+i);
			if (query.indexOf("AND", i)==i+2) {
				System.out.println("Reached AND");
				operands.push("AND");
				i = i + 5;
			} else if (query.indexOf("OR", i)==i+2) {
				System.out.println("Reached OR");
				operands.push("OR");
				i = i + 4;
			} else if ((query.charAt(i-1)==')' && query.indexOf("AND", i)==i+3)) {
				System.out.println("Reached AND 2");
				operands.push("AND");
				i = i + 6;
			} else if ((query.charAt(i-1)==')' && query.indexOf("OR", i)==i+3)) {
				System.out.println("Reached OR 2");
				operands.push("OR");
				i = i + 5;
			} else if (i==query.length()-1 || query.charAt(i+1)==')') {
				System.out.println("Reached End of Input Query String. i is "+i+" and total query length - 1 is "+(query.length()-1));
				continue;
			} else {
				// Invalid query found
				System.out.println("Error in query formation: terminated early");
				return new PageImpl<>(Collections.emptyList());
			}
			
			// continue with the loop search...
		}
		
		System.out.println("Brackets stack: "+brackets.toString());
		if (!brackets.isEmpty()) {
			System.out.println("Error. Brackets are not empty.");
			return new PageImpl<>(Collections.emptyList()); // ill formed query. Check brackets
		}
		System.out.println("Query Results Stack: "+queryResults.toString());
		System.out.println("Combined Results Stack: "+combinedResults.toString());
		// Now that we have all subqueries executed, we need to concatenate them
		combinedResults = combineProcess(combinedResults, operands, queryResults);
		System.out.println("Combined Results (main method)"+combinedResults.toString());
		System.out.println("Query line 2: "+queryResults.toString());
		// Now finalize into Page object
		
		Page<Jog> finalResult = toPage(combinedResults, p);
		
		return finalResult;		
	}
	

	
	private Page<Jog> toPage(List<Jog> list, Pageable pageable) {
		if (pageable.getOffset() >= list.size()) {
			return Page.empty();
		}
		int startIndex = (int)pageable.getOffset();
		int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ?
		list.size() :
		pageable.getOffset() + pageable.getPageSize());
		List<Jog> subList = list.subList(startIndex, endIndex);
		return new PageImpl<Jog>(subList, pageable, list.size());
	}
	
	// This is helper function
	private List<Jog> executeQuery(String query, Pageable p) {
		if (query.length()==0) {
			return Collections.emptyList();
		}
		
		String[] tokens = query.split(" ");
		Page<Jog> result = null;
		if (tokens[0].equals("date")) {
			if (tokens[1].equals("eq")) {
				result = repo.findByDate(tokens[2], p);
			} else if (tokens[1].equals("ne")) {
				result = repo.findByDateNot(tokens[2], p);
			} else if (tokens[1].equals("gt")) {
				result = repo.findByDateGreaterThan(tokens[2], p);
			} else {
				// less than
				result = repo.findByDateLessThan(tokens[2], p);
			}
		} else if (tokens[0].equals("distance")) {
			if (tokens[1].equals("eq")) {
				result = repo.findByDistance(Float.valueOf(tokens[2]), p);
			} else if (tokens[1].equals("ne")) {
				result = repo.findByDistanceNot(Float.valueOf(tokens[2]), p);
			} else if (tokens[1].equals("gt")) {
				result = repo.findByDistanceGreaterThan(Float.valueOf(tokens[2]), p);
			} else {
				// less than
				result = repo.findByDistanceLessThan(Float.valueOf(tokens[2]), p);
			}
		} else if (tokens[0].equals("time")) {
			if (tokens[1].equals("eq")) {
				result = repo.findByTime(tokens[2], p);
			} else if (tokens[1].equals("ne")) {
				result = repo.findByTimeNot(tokens[2], p);
			} else if (tokens[1].equals("gt")) {
				result = repo.findByTimeGreaterThan(tokens[2], p);
			} else {
				// less than
				result = repo.findByTimeLessThan(tokens[2], p);
			}
		} else if (tokens[0].equals("minutes")) {
			if (tokens[1].equals("eq")) {
				result = repo.findByMinutes(Float.valueOf(tokens[2]), p);
			} else if (tokens[1].equals("ne")) {
				result = repo.findByMinutesNot(Float.valueOf(tokens[2]), p);
			} else if (tokens[1].equals("gt")) {
				result = repo.findByMinutesGreaterThan(Float.valueOf(tokens[2]), p);
			} else {
				// less than
				result = repo.findByMinutesLessThan(Float.valueOf(tokens[2]), p);
			}
		} else if (tokens[0].equals("location")) {
			if (tokens[1].equals("eq")) {
				result = repo.findByLocation(tokens[2], p);
			} else if (tokens[1].equals("ne")) {
				result = repo.findByLocationNot(tokens[2], p);
			} else {
				result = null; // invalid location operand
			}
		} 
		
		if (result==null) return null;
		
		List<Jog> temp = result.getContent();
		return temp;
	}

	// This is helper function for processing the stacks
	private List<Jog> combineProcess(List<Jog> combinedResults, Deque<String> operands, Deque<List<Jog>> queryResults) {
		System.out.println("Combiner line 0: "+queryResults.toString());
		if (operands.size()>0) {
			if (operands.peek().equals("AND")) {
				if (combinedResults.isEmpty()) {
					if (queryResults.size()>1) {
						combinedResults = combinerAnd(queryResults.pop(), queryResults.pop());
						System.out.println("Combine inner line A: "+queryResults.toString());
					} else {
						// something went wrong. The query was not well formed.
						System.out.println("Failed in combining AND results. Error 1a.");
						return null;
					}
				} else {
					if (queryResults.size()>0) {
						combinedResults = combinerAnd(combinedResults, queryResults.pop());
						System.out.println("Combine inner line B: "+queryResults.toString());
					} else {
						// something went wrong. The query was not well formed.
						System.out.println("Failed in combining AND results. Error 1b.");
						return null;
					}
				}
			} else {
				// this is OR
				if (combinedResults.isEmpty()) {
					if (queryResults.size()>1) {
						combinedResults = combinerOr(queryResults.pop(), queryResults.pop());
						System.out.println("Combine inner line C: "+queryResults.toString());
					} else {
						// something went wrong. The query was not well formed.
						System.out.println("Failed in combining OR results. Error 2a.");
						return null;
					}
				} else {
					if (queryResults.size()>0) {
						combinedResults = combinerOr(combinedResults, queryResults.pop());
						System.out.println("Combine inner line D: "+queryResults.toString());
					} else {
						// something went wrong. The query was not well formed.
						System.out.println("Failed in combining OR results. Error 2b.");
						return null;
					}
				}
			}
			operands.pop();
		}
		
		System.out.println("Combiner line 1: "+combinedResults.isEmpty()+","+queryResults.size());
		System.out.println("Combiner line 2: "+operands.toString());
		System.out.println("Combiner line 3: "+combinedResults.toString());
		if (combinedResults.isEmpty() && queryResults.size()==1) {
			System.out.println("Reached ; this will only happen if operands stack was always empty from the get-go");
			combinedResults = queryResults.pop();
			
		} else if (combinedResults.isEmpty() && queryResults.size()>1) { 
			System.out.println("there are multiple queries found, but no operands, something is wrong");
			return null;
		}
		return combinedResults;
	}

	// Combine OR helper function
	private List<Jog> combinerOr(List<Jog> res0, List<Jog> res1) {
	
		List<Jog> result = new ArrayList<>();
		result.addAll(res1);
		result.addAll(res0);
		return result;
		
	}
	
	// Combine And helper function
	private List<Jog> combinerAnd(List<Jog> res0, List<Jog> res1) {
	
		List<Jog> result = new ArrayList<>();
		for (int i = 0; i<res0.size(); i++) {
			for (int j = 0; j<res1.size(); j++) {
				System.out.println(res0.get(i).toString());
				System.out.println(res1.get(j).toString());
				System.out.println("---");
				if (res0.get(i).equals(res1.get(j))) {
					result.add(res0.get(i));
					//res1.remove(0);
					continue;
				}
			}
		}
		
		return result;
		
	}
	/*
	@RequestMapping("/jogs/report")
	@ResponseBody
	public Page<Jog> runReport(Pageable p) {
		List<Jog> res = repo.findAll(p);
	}
	*/
	
	@PostMapping(path="/jog", consumes={"application/json"})
	public Jog addJog(@RequestBody Jog jog) {
		WeatherController wc = new WeatherController();
		System.out.println(jog.getLocation());
		jog.setWeather(wc.getWeather(jog.getLocation()));
		repo.save(jog);
		return jog;
	}
	
	
}
