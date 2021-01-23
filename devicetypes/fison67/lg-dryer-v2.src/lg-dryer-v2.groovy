/**
 *  LG Dryer v2(v.0.0.2)
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
import groovy.transform.Field
import groovy.time.TimeCategory

metadata {
	definition (name: "LG Dryer v2", namespace: "fison67", author: "fison67", ocfDeviceType: "oic.d.dryer") {
        capability "Sensor"
        capability "Switch"
        capability "Dryer Mode"
        capability "Dryer Operating State"
        
        command "setStatus"
        
        attribute "leftMinute", "number"
        attribute "curState", "string"
        attribute "processState", "string"
        attribute "course", "string"
	}

	simulator {
	}
    
	preferences {
        input name: "language", title:"Select a language" , type: "enum", required: true, options: ["EN", "KR"], defaultValue: "KR", description:"Language for DTH"
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
    
    sendEvent(name:"supportedMachineStates", value: ["run", "stop"])
}

def setData(dataList){
	for(data in dataList){
        state[data.id] = data.code
    }
}

def getStateStr(state){
	if(language == "EN"){
    	return state
    }else{
        if(state == "INITIAL"){
            return "대기 중"
        }else if(state == "RUNNING"){
            return "건조 중"
        }else if(state == "POWEROFF"){
            return "종료"
        }else if(state == "PAUSE"){
            return "일시정지 중"
        }else if(state == "ERROR"){
            return "에러 발생"
        }else if(state == "END"){
            return "종료 상태"
        }else{
            return state
        }
    }
}

def setDryerJobState(state){
	def value = "none"
    if(state == "INITIAL"){
        value = "weightSensing"
    }else if(state == "RUNNING"){
        value = "drying"
    }else if(state == "POWEROFF"){
        value = "finished"
    }else if(state == "PAUSE"){
        value = "drying"
    }else if(state == "ERROR"){
    }else if(state == "END"){
        value = "finished"
    }
    sendEvent(name:"dryerJobState", value: value)
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
        		sendEvent(name:"machineState", value: report.washerDryer.state == "POWEROFF" ? "stop" : "run")
        		setDryerJobState(report.washerDryer.state)
            }
            
            /**
            * Set a time
            */
        	if(report.washerDryer.remainTimeMinute != null){
            	state.remainTimeMinue = report.washerDryer.remainTimeMinute
            }
       //     state.remainTimeHour = 1
        //    setDryerJobState("RUNNING")
        	if(report.washerDryer.remainTimeHour != null){
            	state.remainTimeHour = report.washerDryer.remainTimeHour
            }
            
    		sendEvent(name:"leftTime", value: changeTime(state.remainTimeHour as int) + ":" + changeTime(state.remainTimeMinue as int) + ":00")
            sendEvent(name:"leftMinute", value: (state.remainTimeHour as int) * 60 + (state.remainTimeMinue as  int))
            
            def currentTime = new Date()
            use( TimeCategory ) {
                currentTime = currentTime + (state.remainTimeHour as int).hours
                currentTime = currentTime + (state.remainTimeMinue as int).minutes
            }

//            def dateStr = currentTime.format('yyyy-MM-dd HH:mm:ss')
            def dateStr = currentTime.format("yyyy-MM-dd'T'HH:mm:ss+09:00", location.timeZone)
            sendEvent(name:"completionTime", value: dateStr)
            log.debug dateStr
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
