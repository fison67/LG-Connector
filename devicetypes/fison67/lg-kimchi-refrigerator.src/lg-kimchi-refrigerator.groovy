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
/*        
        valueTile("temp1_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Temp'
        }
        controlTile("temperatureControl", "device.level", "slider", range:"(0..6)", height: 1, width: 1) {
            state "level", action:"setLevel"
        }
        valueTile("temp2_label", "", decoration: "flat", width: 2, height: 1) {
            state "default", label:'Freezer Temp'
        }
        
        controlTile("temperature2Control", "device.level2", "slider", range:"(16..24)", height: 1, width: 1) {
            state "level", action:"setLevel2"
        }
        
        valueTile("airFresh_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Air Fresh'
        }
        valueTile("airFresh", "device.airFresh", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        valueTile("icePlus_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'IcePlus'
        }
        valueTile("icePlus", "device.icePlus", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}', action: "setIcePlusToggle"
        }
        
        valueTile("smartSavingMode_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Smart Saving Mode'
        }
        valueTile("smartSavingMode", "device.smartSavingMode", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        
        valueTile("locking_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Locking'
        }
        valueTile("lock", "device.lock", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
*/        
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
    /*
    if(jsonObj.DoorOpenState){
        sendEvent(name:"contact", value: (jsonObj.DoorOpenState.rValue == "CLOSE" ? "closed" : "open"))
        if(jsonObj.DoorOpenState.rValue == "OPEN"){
            sendEvent(name:"lastOpen", value: new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone))
        }
    }
    
    if(jsonObj.TempRefrigerator){
    	sendEvent(name:"temperature", value: jsonObj.TempRefrigerator.rValue as int)
    	sendEvent(name:"level", value: jsonObj.TempRefrigerator.rValue as int)
	}
    if(jsonObj.TempFreezer){
    	sendEvent(name:"temperature2", value: jsonObj.TempFreezer.rValue as int)
    	sendEvent(name:"level2", value: -(jsonObj.TempFreezer.rValue as int))
    }
    if(jsonObj.FreshAirFilter){
    	sendEvent(name:"airFresh", value: jsonObj.FreshAirFilter.sValue)
    }
    if(jsonObj.SmartSavingModeStatus){
    	sendEvent(name:"smartSavingMode", value: jsonObj.SmartSavingModeStatus.rValue.toLowerCase())
    }
    if(jsonObj.LockingStatus){
    	sendEvent(name:"lock", value: jsonObj.LockingStatus.rValue == "UNLOCK" ? "unlocked" : "locked")
    }
    if(jsonObj.IcePlus){
    	sendEvent(name:"icePlus", value: jsonObj.IcePlus.value == 2 ? "on" : "off")
    }
    */
    updateLastTime();
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def setLevel(level){
    makeCommand("setTemp", level)
}

def setLevel2(level){
    makeCommand("setFreezerTemp", -(level))
}

def setSmartSavingOn(value){
    makeCommand("setActiveSaving", value)
}

def setIcePlusToggle(){
    def status = device.currentValue("icePlus")
    if(status == "on"){
    	setIcePlusOff()
    }else{
    	setIcePlusOn()
    }
}

def setIcePlusOn(){
    makeCommand("setIcePlus", "on")
}

def setIcePlusOff(){
    makeCommand("setIcePlus", "off")
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
