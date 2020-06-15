/**
 *  LG Warsher v2(v.0.0.1)
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
	definition (name: "LG Washer v2", namespace: "fison67", author: "fison67") {
        capability "Switch"
        capability "Sensor"
        capability "Switch Level"
        capability "Configuration"
        
        command "setStatus"
        
        attribute "processState", "string"
        attribute "leftTime", "string"
        attribute "leftMinute", "number"
        attribute "targetTemperature", "string"
	}

	simulator {
	}
    
	preferences {}

	tiles(scale: 2) {
		
        multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState("on", label:'${name}', backgroundColor:"#00a0dc", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-washer.png?raw=true")
                attributeState("off", label:'${name}', backgroundColor:"#ffffff",  icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-washer.png?raw=true")
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"setLevel"
            }     
		}
        
        valueTile("curState_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'State'
        }
        valueTile("curState", "device.curState", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("targetTemperature_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Temperature'
        }
        valueTile("targetTemperature", "device.targetTemperature", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("leftTime_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Left Time'
        }
        valueTile("leftTime", "device.leftTime", decoration: "flat", width: 3, height: 1) {
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

def getStateStr(state){
	if(state == "INITIAL"){
    	return "대기 중"
    }else if(state == "DETECTING"){
    	return "옷감량 확인 중"
    }else if(state == "POWEROFF"){
    	return "종료"
    }else if(state == "PAUSE"){
    	return "일시정지 중"
    }else if(state == "ERROR"){
    	return "에러 발생"
    }else if(state == "END"){
    	return "종료 상태"
	}else if(state == "RUNNING"){
		return "세탁 중"
    }else if(state == "PREWASH"){
		return "애벌세탁 중"
    }else if(state == "RINSING"){
		return "헹굼 중"
    }else if(state == "FRESHCARE"){
		return "구김방지 중"
    }else if(state == "SPINNING"){
		return "탈수 중"
	}else if(state == "DRYING"){
		return "건조 중"
    }else{
    	return state
    }
	
}

def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    
    def jsonObj = new JsonSlurper().parseText(data.data)
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report.washerDryer != null){
        	if(report.washerDryer.state != null){
        		sendEvent(name:"processState", value: getStateStr(report.washerDryer.state))
                sendEvent(name:"switch", value: report.washerDryer.state == "POWEROFF" ? "off" : "on")
                
            }
            
            if(report.washerDryer.temp != null){
            	def temp = report.washerDryer.temp.split("_")[1]
        		sendEvent(name:"targetTemperature", value: (report.washerDryer.temp == "NO_TEMP" ? "NO" : temp) )
            }
            
            
            /**
            * Set a time
            */
        	if(report.washerDryer.remainTimeMinute != null){
            	state.remainTimeMinue = report.washerDryer.remainTimeMinute
            }
        	if(report.washerDryer.remainTimeHour != null){
            	state.remainTimeHour = report.washerDryer.remainTimeHour
            }
    		sendEvent(name:"leftTime", value: changeTime(state.remainTimeHour as int) + ":" + changeTime(state.remainTimeMinue as int) + ":00", displayed: false)
            sendEvent(name:"leftMinute", value: (state.remainTimeHour as int) * 60 + (state.remainTimeMinue as  int), displayed: false)
            
        }
    }
    
    updateLastTime();
}

def changeTime(time){
	if(time < 10){
    	return "0" + time
    }
    return "" + time
}

def updateLastTime(){
	def now = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
    sendEvent(name: "lastCheckin", value: now, displayed:false)
}

def on(){}

def off(){}

def makeCommand(type, value){
    def body = [
        "id": state.id,
        "cmd": type,
        "data": value
    ]
    def options = makeCommand(body)
    sendCommand(options, null)
}

def makeCommand(body){
	def options = [
     	"method": "POST",
        "path": "/tv/control",
        "headers": [
        	"HOST": parent.getServerAddress(),
            "Content-Type": "application/json"
        ],
        "body":body
    ]
    return options
}

def sendCommand(options, _callback){
	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}
