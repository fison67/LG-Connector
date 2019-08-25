/**
 *  LG Kimchi Refrigerator(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 fison67@nate.com
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
	definition (name: "LG Kimchi Refrigerator", namespace: "fison67", author: "fison67", mnmn:"SmartThings", vid: "generic-contact") {
        capability "Sensor"
        capability "Contact Sensor"
        capability "Switch Level"
        capability "Temperature Measurement"
        capability "Refresh"
        
        command "setStatus"
        
        attribute "leftOrTopRoom", "number"
        attribute "middlemRoom", "string"
        attribute "bottomRoom", "number"
	}

	simulator {
	}
    
	preferences {
    
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
       
       
        valueTile("leftOrTopRoom_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'LeftOrTopRoom'
        }
        valueTile("leftOrTopRoom", "device.leftOrTopRoom", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}', action: "setIcePlusToggle"
        }
        
        valueTile("BottomRoom_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'BottomRoom'
        }
        valueTile("bottomRoom", "device.bottomRoom", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
      
        valueTile("MiddleRoom_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'MiddleRoom'
        }
        valueTile("middlemRoom", "device.middlemRoom", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }

        valueTile("airFresh_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Air Fresh'
        }
        valueTile("airFresh", "device.airFresh", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        valueTile("locking_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Locking'
        }
        valueTile("lock", "device.lock", decoration: "flat", width: 3, height: 1) {
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

def updated() {
}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def setData(dataList){
	for(data in dataList){
        state[data.id] = data.code
    }
}

def setStatus(data){
	log.debug "${data.data}"
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    if(jsonObj.DoorOpenState){
        sendEvent(name:"contact", value: (jsonObj.DoorOpenState.rValue == "CLOSE" ? "closed" : "open"))
        if(jsonObj.DoorOpenState.rValue == "OPEN"){
            sendEvent(name:"lastOpen", value: new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone))
        }
    }
    if(jsonObj.LeftOrTopRoom){
    	sendEvent(name:"leftOrTopRoom", value: jsonObj.LeftOrTopRoom.rValue as int)
    }
    if(jsonObj.MiddleRoom){
    	def value = "Middle"
    	switch(jsonObj.MiddleRoom.value){
       	case 0:
        	value = "Middle"
        	break
        case 1:
        	value = "Strong"
        	break
        case 2:
        	value = "Weak"
        	break
        }
    	sendEvent(name:"middlemRoom", value: value)
    }
    if(jsonObj.BottomRoom){
    	sendEvent(name:"bottomRoom", value: jsonObj.BottomRoom.rValue as int)
    }
    if(jsonObj.LockingStatus){
    	sendEvent(name:"lock", value: jsonObj.LockingStatus.rValue == "UNLOCK" ? "unlocked" : "locked")
    }
    
    
    if(jsonObj.FreshAirFilter){
    	sendEvent(name:"airFresh", value: jsonObj.FreshAirFilter.sValue)
    }
   
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def makeCommand(command, value){
    def body = [
        "id": state.id,
        "command": command,
        "value": value
    ]
    def options = _makeCommand(body)
    sendCommand(options, null)
}

def _makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/devices/control2",
        "headers": [
        	"HOST": state.app_url,
            "Content-Type": "application/json"
        ],
        "body":body
    ]
    log.debug options
    return options
}

def sendCommand(options, _callback){
	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}
