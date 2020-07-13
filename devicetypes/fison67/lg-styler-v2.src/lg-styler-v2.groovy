/**
 *  LG Styler v2(v.0.0.1)
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
import groovy.transform.Field

metadata {
	definition (name: "LG Styler v2", namespace: "fison67", author: "fison67") {
        capability "Sensor"
        capability "Switch"
        
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

	tiles(scale: 2) {
		
        multiAttributeTile(name:"switch", type: "generic", width: 6, height: 2){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState("on", label:'${name}', backgroundColor:"#00a0dc", icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-washer.png?raw=true")
                attributeState("off", label:'${name}', backgroundColor:"#ffffff",  icon:"https://github.com/fison67/LG-Connector/blob/master/icons/lg-washer.png?raw=true")
			}
            
			tileAttribute("device.lastCheckin", key: "SECONDARY_CONTROL") {
    			attributeState("default", label:'Updated: ${currentValue}')
            }
		}
        valueTile("processState_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Process State'
        }
        valueTile("processState", "device.processState", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        valueTile("course_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Course'
        }
        valueTile("course", "device.course", decoration: "flat", width: 3, height: 1) {
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

def getCoureStr(cource){
	if(language == "EN"){
    	return state
    }else {
    	if(state == "STANDARD"){
            return "스타일링 표준"
        }else if(state == "QUICK"){
            return "스타일링 급속"
        }else if(state == "STRONG"){
            return "스타일링 강력"
        }else if(state == "SUITS_COATS"){
            return "고급의류 정장/코트"
        }else if(state == "WOOL_KNITTED"){
            return "고급의류 울/니트"
        }else if(state == "COURSE_CLOTH_FUNTIONAL"){
            return "고급의류 기능성"
        }else if(state == "SANITARY_STANDARD"){
            return "위생살균 침구"
        }else if(state == "BEDDINGS"){
            return "위생살균 침구"
        }else if(state == "FINEDUST"){
            return "맞춤건조 자동건조"
        }else if(state == "VIRUS"){
            return "위생살균 바이러스"
        }else if(state == "TIME_DRY_INSIDE_120"){
            return "예약"
        }else{
            return state
        }
    }
}

def getStateStr(state){
	if(language == "EN"){
    	return state
    }else{
        if(state == "INITIAL"){
            return "대기 중"
        }else if(state == "RUNNING"){
            return "동작 중"
        }else if(state == "POWEROFF"){
            return "종료"
        }else if(state == "PAUSE"){
            return "일시정지 중"
        }else if(state == "ERROR"){
            return "에러 발생"
        }else if(state == "COMPLETE"){
            return "종료 상태"
        }else if(state == "DIAGNOSIS"){
            return "진단 중"
        }else if(state == "COOLING"){
            return "쿨링 중"
        }else if(state == "DRYING"){
            return "건조 중"
        }else{
            return state
        }
    }
}

def setStatus(data){
	log.debug "Update >> ${data.key} >> ${data.data}"
    
    def jsonObj = new JsonSlurper().parseText(data.data)
    if(jsonObj.data.state.reported != null){
    	def report = jsonObj.data.state.reported
        
        if(report.styler != null){
        	if(report.styler.state != null){
        		sendEvent(name:"processState", value: getStateStr(report.washerDryer.state))
                sendEvent(name:"switch", value: report.washerDryer.state == "POWEROFF" ? "off" : "on")
            }
            
            if(report.styler.course != null){
        		sendEvent(name:"course", value: getCoureStr(report.styler.course))
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
