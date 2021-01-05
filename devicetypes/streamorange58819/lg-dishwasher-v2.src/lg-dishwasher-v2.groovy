/**
 *  LG DishWasher v2(v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2019 fison67@nate.com
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
import groovy.time.TimeCategory

metadata {
	definition (name: "LG DishWasher v2", namespace: "streamorange58819", author: "fison67", mnmn: "fison67", vid: "8e45665f-598f-3ae2-aa37-aee8b4145d25", ocfDeviceType: "oic.d.dishwasher") {
        capability "Dishwasher Operating State"
        capability "Contact Sensor"
        capability "Switch"
        capability "streamorange58819.autodoor"
        capability "streamorange58819.hightemp"
        capability "streamorange58819.extradry"
        capability "streamorange58819.rinserefill"
        capability "streamorange58819.saltrefill"
        capability "streamorange58819.dualzone"
        capability "streamorange58819.halfload"
        capability "streamorange58819.rinselevel"
        capability "streamorange58819.softeninglevel"
        
        command "setStatus"
        
        attribute "leftMinute", "number"
        attribute "leftTime", "string"
	}

}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def installed(){
    sendEvent(name:"supportedMachineStates", value: ["run", "stop", "pause"])
    sendEvent(name:"dishwasherJobState", value: "unknown")
}

def updated() {}

def setInfo(String app_url, String address) {
	log.debug "${app_url}, ${address}"
	state.app_url = app_url
    state.id = address
}

def setDishWasherJobState(process){
	def value = ""
    if(process == "DRYING"){
    	value = "drying"
    }else if(process == "RUNNING"){
    	value = "wash"
    }else if(process == "RINSING"){
    	value = "rinse"
    }else if(process == "RESERVED"){
    }else if(process == "NONE"){
    }else if(process == "CANCEL"){
    	value = "finish"
    }else if(process == "NIGHTDRY"){
    	value = "drying"
    }else if(process == "END"){
    	value = "finish"
    }
    if(value != ""){
    	sendEvent(name:"dishwasherJobState", value: value)
    }
}

def setDishWasherMachineState(state){
	def value = "unknown"
    if(state == "STANDBY" || state == "INITIAL" || state == "POWERFAIL"){
    }else if(state == "RUNNING"){
        value = "run"
    }else if(state == "POWEROFF" || state == "END"){
        value = "stop"
    }
    sendEvent(name:"machineState", value: value)
}

def setStatus(data){
//	log.debug "Update >> ${data.key} >> ${data.data}"
    
    def jsonObj = new JsonSlurper().parseText(data.data)
    if(jsonObj.data.state.reported != null){
    	
    	//log.debug jsonObj.data.state.reported.dishwasher.toString()
        log.debug groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(jsonObj.data.state.reported.dishwasher))
        
    	def report = jsonObj.data.state.reported
        if(report.dishwasher != null){
        	if(report.dishwasher.door){
            	sendEvent(name:"contact", value: report.dishwasher.door == "OPEN" ? "open" : "closed")
            }
        
        	if(report.dishwasher.state != null){
        		setDishWasherMachineState(report.dishwasher.state)
            }
            
            if(report.dishwasher.process != null){
        		setDishWasherJobState(report.dishwasher.process)
    			sendEvent(name:"switch", value: report.dishwasher.process == "RUNNING" ? "on" : "off")
            }
            
           	if(report.dishwasher.autoDoor){
    			sendEvent(name:"autoDoor", value: report.dishwasher.autoDoor.toLowerCase())
            }
            
            if(report.dishwasher.course){
            
            }
            
            if(report.dishwasher.highTemp){
    			sendEvent(name:"highTemp", value: report.dishwasher.highTemp.toLowerCase())
            }
            
            if(report.dishwasher.extraDry){
    			sendEvent(name:"extraDry", value: report.dishwasher.extraDry.toLowerCase())
            }
            
            if(report.dishwasher.rinseLevel){
    			sendEvent(name:"rinseLevel", value: report.dishwasher.rinseLevel)
            }
            
            if(report.dishwasher.softeningLevel){
    			sendEvent(name:"softeningLevel", value: report.dishwasher.softeningLevel)
            }
            
            if(report.dishwasher.rinseRefill){
    			sendEvent(name:"rinseRefill", value: report.dishwasher.rinseRefill == "SET" ? "on" : "off")
            }
            
            if(report.dishwasher.saltRefill){
    			sendEvent(name:"saltRefill", value: report.dishwasher.saltRefill == "SET" ? "on" : "off")
            }
            
            if(report.dishwasher.dualZone){
    			sendEvent(name:"dualZone", value: report.dishwasher.dualZone.toLowerCase())
            }
            
            if(report.dishwasher.halfLoad){
    			sendEvent(name:"halfLoad", value: report.dishwasher.halfLoad.toLowerCase())
            }
            
            
            
            /**
            * Set a time
            */
        	if(report.dishwasher.remainTimeMinute != null){
            	state.remainTimeMinue = report.dishwasher.remainTimeMinute
            }
        	if(report.dishwasher.remainTimeHour != null){
            	state.remainTimeHour = report.dishwasher.remainTimeHour
            }
            
    		sendEvent(name:"leftTime", value: changeTime(state.remainTimeHour as int) + ":" + changeTime(state.remainTimeMinue as int) + ":00")
            sendEvent(name:"leftMinute", value: (state.remainTimeHour as int) * 60 + (state.remainTimeMinue as  int))
            
            def currentTime = new Date()
            use( TimeCategory ) {
                currentTime = currentTime + (state.remainTimeHour as int).hours
                currentTime = currentTime + (state.remainTimeMinue as int).minutes
            }

//            def dateStr = currentTime.format('yyyy-MM-dd HH:mm:ss')
            sendEvent(name:"completionTime", value: currentTime.format("yyyy-MM-dd'T'HH:mm:ss+09:00", location.timeZone))
            
        }
    }
}

def changeTime(time){
	if(time < 10){
    	return "0" + time
    }
    return "" + time
}
