/**
 *  LG Oven v2(v.0.0.1)
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
	definition (name: "LG Oven v2", namespace: "fison67", author: "fison67") {
        capability "Sensor"
        capability "Switch"
        
        command "setStatus"
        
        attribute "leftMinute", "number"
        attribute "curState", "string"
        attribute "processState", "string"
        attribute "course", "string"
        attribute "targetTemperature", "number"
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
        valueTile("temperature_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'Target Temperature'
        }
        valueTile("targetTemperature", "device.targetTemperature", decoration: "flat", width: 3, height: 1) {
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
	if(language == "EN"){
    	return state
    }else {
    	if(state == "INITIAL"){
            return "대기 중"
        }else if(state == "CLEANING"){
            return "청소 중"
        }else if(state == "PREHEATING"){
            return "예열 중"
        }else if(state == "COOKING_IN_PROGRESS"){
            return "쿠킹 중"
        }else if(state == "PAUSED"){
            return "일시정지 중"
        }else if(state == "DONE"){
            return "완료"
        }else if(state == "PREHEATING_IS_DONE"){
            return "예열 완료"
        }else if(state == "PREFERENCE"){
            return "설정 중"
        }else{
            return state
        }
    }
}

def getCoureStr(state){
	if(language == "EN"){
    	return state
    }else{
        if(state == "INITIAL"){
            return "대기"
        }else if(state == "OVEN"){
            return "오븐"
        }else if(state == "RANGE"){
            return "레인지"
        }else if(state == "RANGE_STEAMelse"){
            return "스팀"
        }else if(state == "BAKE"){
            return "구이"
        }else if(state == "MULTICLEAN"){
            return "청소"
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
        
        if(report.ovenState != null){
        	if(report.ovenState.LWOManualCookName != null){
        		sendEvent(name:"cource", value: getCoureStr(report.ovenState.LWOManualCookName))
                sendEvent(name:"switch", value: report.ovenState.LWOManualCookName == "INITIAL" ? "off" : "on")
            }
        	if(report.ovenState.LWOState != null){
        		sendEvent(name:"processState", value: getStateStr(report.ovenState.LWOState))
            }
            
            if(report.ovenState.LWOTargetTemperatureC != null){
        		sendEvent(name:"targetTemperature", value: report.ovenState.LWOTargetTemperatureC)
            }
            
            /**
            * Set a time
            */
        	if(report.ovenState.LWORemainTimeSecond != null){
            	state.remainTimeSecond = report.ovenState.LWORemainTimeSecond
            }
        	if(report.ovenState.LWORemainTimeMinute != null){
            	state.remainTimeMinue = report.ovenState.LWORemainTimeMinute
            }
        	if(report.ovenState.LWORemainTimeHour != null){
            	state.remainTimeHour = report.ovenState.LWORemainTimeHour
            }
            
    		sendEvent(name:"leftTime", value: changeTime(state.remainTimeHour as int) + ":" + changeTime(state.remainTimeMinue as int) + ":" + changeTime(state.remainTimeSecond as int), displayed: false)
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
