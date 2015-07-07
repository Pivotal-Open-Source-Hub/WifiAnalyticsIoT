package io.pivotal.demo.ui;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/controller")
public class Controller {
    
    public Controller() {
        
    }

    @RequestMapping(method = RequestMethod.GET)
    public void /*PagedCities*/ list() {
        // return repository.findAll(pageable.getPageNumber(), pageable.getPageSize());
    }
    
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public void /*PagedCities*/ search(@RequestParam("name") String name) {
        //return repository.findByNameContains(name, pageable.getPageNumber(), pageable.getPageSize());
    }
    
    @RequestMapping(value="/getDeviceMap")
    public @ResponseBody DeviceMap getDeviceMap(){
    	System.out.println("Called");
        DeviceMap map = new DeviceMap();
        
        for (int i=0; i<200; i++){
	        map.addDevice("i"+i, (int)(Math.random()*100+Math.random()*100), (int)(Math.random()*100+Math.random()*100));
        }
    	return map;
    }    
    
}