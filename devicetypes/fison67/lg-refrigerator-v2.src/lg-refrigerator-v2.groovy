/**
 *  LG Refrigerator v2(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2020 fison67@nate.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
*/
 
import groovy.json.JsonSlurper

metadata {
	definition (name: "LG Refrigerator v2", namespace: "fison67", author: "fison67", mnmn:"SmartThings", vid: "generic-contact") {
        capability "Sensor"
        capability "Contact Sensor"
        
        command "setStatus"
        
        attribute "mode", "string"
        attribute "lastOpen", "string"
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
	}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"contact", type: "generic", width: 6, height: 2){
            tileAttribute ("device.contact", key: "PRIMARY_CONTROL") {
               	attributeState "open", label:'${name}', icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-refrigerator.png?raw=true", backgroundColor:"#e86d13"
            	attributeState "closed", label:'${name}', icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-refrigerator.png?raw=true", backgroundColor:"#00a0dc"
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
		}
       
       
        valueTile("mode_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Mode'
        }
        valueTile("mode", "device.mode", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
             
        valueTile("lastOpen_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Last\nOpen'
        }
        valueTile("lastOpen", "device.lastOpen", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report["refState"] != null){
        	def refState = report["refState"]
            
            if(refState["atLeastOneDoorOpen"] != null){
        		sendEvent(name:"contact", value: (refState["atLeastOneDoorOpen"] == "CLOSE" ? "closed" : "open"))
                if(refState["atLeastOneDoorOpen"] == "OPEN"){
                    sendEvent(name:"lastOpen", value: new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone))
                }
            }
            
            if(refState["drawerMode"] != null){
        		sendEvent(name:"mode", value: refState["drawerMode"])
            }
        }
        
    }
   
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}
