// Bismillah Hirrahman Nirrahim

package com.toptal.setup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toptal.JogRepository;
import com.toptal.OrganizationRepository;
import com.toptal.PrivilegeRepository;
import com.toptal.UserRepository;
import com.toptal.controller.WeatherController;
import com.toptal.model.Jog;
import com.toptal.model.Organization;
import com.toptal.model.Privilege;
import com.toptal.model.Role;
import com.toptal.model.User;

@Component
public class SetupData {
    @Autowired
    private UserRepository userRepository;

    //@Autowired
    private PrivilegeRepository privilegeRepository;

    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private JogRepository jogRepository;
    
    @PostConstruct
    public void init() {
        //initPrivileges();
        //initOrganizations();
        //initUsers();
    	//initJogs();
    }
    
    /*
    private void initPrivileges() {
        Privilege privilege1 = new Privilege("READ_USER");
        privilegeRepository.save(privilege1);

        Privilege privilege2 = new Privilege("WRITE_USER");
        privilegeRepository.save(privilege2);
        
        Privilege privilege3 = new Privilege("DELETE_USER");
        privilegeRepository.save(privilege3);
        
        Privilege privilege4 = new Privilege("UPDATE_USER");
        privilegeRepository.save(privilege4);
        
        Privilege privilege1A = new Privilege("READ_RECORD");
        privilegeRepository.save(privilege1A);

        Privilege privilege2A = new Privilege("WRITE_RECORD");
        privilegeRepository.save(privilege2A);
        
        Privilege privilege3A = new Privilege("DELETE_RECORD");
        privilegeRepository.save(privilege3A);
        
        Privilege privilege4A = new Privilege("UPDATE_RECORD");
        privilegeRepository.save(privilege4A);
        
    }
    
    private void initOrganizations() {
        Organization org1 = new Organization(Role.USER);
        organizationRepository.save(org1);
        
        Organization org2 = new Organization(Role.MANAGER);
        organizationRepository.save(org2);
        
        Organization org3 = new Organization(Role.ADMIN);
        organizationRepository.save(org3);
    }
    */
    private void initUsers() {
        /*
        Privilege privilege1 = privilegeRepository.findByName("READ_USER");
        privilegeRepository.save(privilege1);

        Privilege privilege2 = privilegeRepository.findByName("WRITE_USER");
        privilegeRepository.save(privilege2);
        
        Privilege privilege3 = privilegeRepository.findByName("DELETE_USER");
        privilegeRepository.save(privilege3);
        
        Privilege privilege4 = privilegeRepository.findByName("UPDATE_USER");
        privilegeRepository.save(privilege4);
        
        Privilege privilege1A = privilegeRepository.findByName("READ_RECORD");
        privilegeRepository.save(privilege1A);

        Privilege privilege2A = privilegeRepository.findByName("WRITE_RECORD");
        privilegeRepository.save(privilege2A);
        
        Privilege privilege3A = privilegeRepository.findByName("DELETE_RECORD");
        privilegeRepository.save(privilege3A);
        
        Privilege privilege4A = privilegeRepository.findByName("UPDATE_RECORD");
        privilegeRepository.save(privilege4A);
        */
        User user1 = new User();
        user1.setUsername("john");
        user1.setPassword("123");
        /*
        Set<Privilege> p1 = new HashSet<>();
        p1.add(privilege1);
        p1.add(privilege2);
        p1.add(privilege3);
        p1.add(privilege4);
        p1.add(privilege1A);
        p1.add(privilege2A);
        p1.add(privilege3A);
        p1.add(privilege4A);
        */
        user1.setOrganization(organizationRepository.findByName(Role.ADMIN));
        //user1.getOrganization().setPrivileges(p1);
        //user1.setId(1L);
        userRepository.save(user1);
        /*
        User user2 = new User();
        user2.setUsername("tom");
        user2.setPassword("111");
        user2.getOrganization().setPrivileges(new HashSet<Privilege>(Arrays.asList(privilege1A, privilege2A, 
        		privilege3A, privilege4A)));
        user2.setOrganization(organizationRepository.findByName("USER"));
        user2.setId(2L);
        userRepository.save(user2);
        */
    }
    
    private void initJogs() {
        Jog temp = new Jog();
        User user = userRepository.getUserByUsername("john");
        temp.setUser(user);
        temp.setDate(LocalDate.now().toString());
        temp.setTime(LocalTime.now().toString());
        temp.setMinutes(60L);
        temp.setDistance(10L);
        temp.setLocation("Toronto");
        temp.setId(1L);
        WeatherController wc = new WeatherController();
		System.out.println(temp.getLocation());
		temp.setWeather(wc.getWeather(temp.getLocation()));
		jogRepository.save(temp);
        
    }
}
