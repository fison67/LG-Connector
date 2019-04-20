/**
 *  LG Refrigerator(v.0.0.3)
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
	definition (name: "LG Refrigerator", namespace: "fison67", author: "fison67", mnmn:"SmartThings", vid: "generic-contact") {
        capability "Sensor"
        capability "Contact Sensor"
        capability "Switch Level"
        capability "Temperature Measurement"
        capability "Refresh"
        
        command "setStatus"
        command "setLevel2"
        command "setSmartSaving", ["number"]
        command "setIcePlusToggle"
        command "setIcePlusOn"
        command "setIcePlusOff"
        
        attribute "icePlus", "string"
        
	}

	simulator {
	}
    
	preferences {
    
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
	def myhubAction = new hubitat.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}
