/**
 *  LG Dryer (v.0.0.1)
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

@Field
STATE_VALUE = [
    0: [val: "@WM_STATE_POWER_OFF_W", str: ["EN":"OFF", "KR":"OFF"] ],
    1: [val: "@WM_STATE_INITIAL_W", str: ["EN":"INITIAL", "KR":"대기 중"] ],
    2: [val: "@WM_STATE_RUNNING_W", str: ["EN":"RUNNING", "KR":"건조 중"] ],
    3: [val: "@WM_STATE_PAUSE_W", str: ["EN":"PAUSE", "KR":"일시정지 중"] ],
    4: [val: "@WM_STATE_END_W", str: ["EN":"END", "KR":"종료 상태"] ],
    5: [val: "@WM_STATE_ERROR_W", str: ["EN":"ERROR", "KR":"에러 발생"] ],
    6: [val: "@WM_STATE_TEST1_W", str: ["EN":"?", "KR":"?"] ],
    7: [val: "@WM_STATE_TEST2_W", str: ["EN":"?", "KR":"?"] ],
    8: [val: "@WM_STATE_SMART_DIAGNOSIS_W", str: ["EN":"SMART DIAGNOSIS", "KR":"스마트 진단 중"] ],
    100: [val: "@WM_STATE_RESERVE_W", str: ["EN":"RESERV", "KR":"예약 중"] ]
]

@Field
PROCESS_STATE_VALUE = [
    0: [val: "@WM_STATE_DETECTING_W", str: ["EN":"DETECTING", "KR":"옷감량 확인 중"] ],
    1: [val: "@WM_STATE_STEAM_W", str: ["EN":"STEAM", "KR":"스팀 중"] ],
    2: [val: "@WM_STATE_DRY_W", str: ["EN":"DRY", "KR":"건조 중"] ],
    3: [val: "@WM_STATE_DRY_W", str: ["EN":"DRY", "KR":"건조 중"] ],
    4: [val: "@WM_STATE_DRY_W", str: ["EN":"DRY", "KR":"건조 중"] ],
    5: [val: "@WM_STATE_COOLING_W", str: ["EN":"COOLING", "KR":"쿨링"] ],
    6: [val: "@WM_STATE_ANTI_CREASE_W", str: ["EN":"ANTI CREASE", "KR":"구김방지 중"] ],
    7: [val: "@WM_STATE_END_W", str: ["EN":"END", "KR":"종료 상태"] ]
]

@Field
COURSE_VALUE = [
	1: [val: "@REFRESH", str: ["EN":"REFRESH", "KR":"REFRESH"] ],
	2: [val: "@WM_DRY24_COURSE_COTTON_SOFT_W", str: ["EN":"COTTON SOFT", "KR":"타월"] ],
	4: [val: "@WM_DRY24_COURSE_BULKY_ITEM_W", str: ["EN":"BULKY", "KR":"이불"] ],
	5: [val: "@WM_DRY24_COURSE_EASY_CARE_W", str: ["EN":"EASY_CARE", "KR":"셔츠"] ],
	7: [val: "@WM_DRY24_COURSE_COTTON_W", str: ["EN":"COURSE COTTON", "KR":"표준"] ],
	8: [val: "@WM_DRY24_COURSE_SPORTS_WEAR_W", str: ["EN":"SPORTS WEAR", "KR":"기능성의류"] ],
	9: [val: "@WM_DRY24_COURSE_QUICK_DRY_W", str: ["EN":"QUICK DRY", "KR":"소량급속"] ],
	11: [val: "@WM_DRY24_COURSE_WOOL_W", str: ["EN":"WOOL", "KR":"울/섬세"] ],
	12: [val: "@WM_DRY24_COURSE_RACK_DRY_W", str: ["EN":"RACK DRY", "KR":"선반건조"] ],
	13: [val: "@WM_DRY24_COURSE_COOL_AIR_W", str: ["EN":"COOL AIR", "KR":"송풍"] ],
	14: [val: "@WM_DRY24_COURSE_WARM_AIR_W", str: ["EN":"WARM AIR", "KR":"온풍"] ],
    15: [val: "@WM_DRY24_COURSE_BEDDING_BRUSH_W", str: ["EN":"BEDDING BRUSH", "KR":"침구털기"] ],
    16: [val: "@WM_DRY24_COURSE_STERILIZATION_W", str: ["EN":"STERILIZATION", "KR":"살균"] ],
    17: [val: "@WM_DRY24_COURSE_POWER_W", str: ["EN":"STERILIZATION", "KR":"강력"] ],
    102: [val: "@WM_WW_DRYER_SMARTCOURSE_GYM_CLOTHES_W", str: ["EN":"GYM CLOTHES", "KR":"운동복"] ],
    103: [val: "@WM_WW_DRYER_SMARTCOURSE_BLANKET_W", str: ["EN":"BLANKET", "KR":"이불"] ],
    104: [val: "@WM_WW_DRYER_SMARTCOURSE_BLANKET_REFRESH_W", str: ["EN":"BLANKET REFRESH", "KR":"이불 REFRESH"] ],
    105: [val: "@WM_WW_DRYER_SMARTCOURSE_RAINY_SEASON_W", str: ["EN":"RAINY SEASON", "KR":"장마철 세탁"] ],
    106: [val: "@WM_WW_DRYER_SMARTCOURSE_SINGLE_GARMENTS_W", str: ["EN":"SINGLE GARMENTS", "KR":"한벌 세탁"] ],
    107: [val: "@WM_WW_DRYER_SMARTCOURSE_DEODORIZATION_W", str: ["EN":"DEODORIZATION", "KR":"냄새 제거"] ],
    108: [val: "@WM_WW_DRYER_SMARTCOURSE_SMALL_LOAD_W", str: ["EN":"SMALL LOAD", "KR":"소량 건조"] ],
    109: [val: "@WM_WW_DRYER_SMARTCOURSE_LINGERIE_W", str: ["EN":"LINGERIE", "KR":"란제리"] ],
    110: [val: "@WM_WW_DRYER_SMARTCOURSE_EASY_IRON_W", str: ["EN":"LINGERIE", "KR":"촉촉 건조"] ],
    111: [val: "@WM_WW_DRYER_SMARTCOURSE_SUPER_DRY_W", str: ["EN":"SUPER DRY", "KR":"강력 건조"] ],
    112: [val: "@WM_WW_DRYER_SMARTCOURSE_ECONOMIC_DRY_W", str: ["EN":"ECONOMIC DRY", "KR":"절약 건조"] ],
    113: [val: "@WM_WW_DRYER_SMARTCOURSE_BIG_SIZE_ITEM_W", str: ["EN":"BIG SIZE ITEM", "KR":"큰 빨래 건조"] ],
    114: [val: "@WM_WW_DRYER_SMARTCOURSE_MINIMIZE_WRINKLES_W", str: ["EN":"MINIMIZE WRINKLES", "KR":"구김 완화 건조"] ],
    115: [val: "@WM_WW_DRYER_SMARTCOURSE_SHOES_FABRIC_DOLL_W", str: ["EN":"SHOES FABRIC DOLL", "KR":"신발/인형 건조"] ],
    116: [val: "@WM_WW_DRYER_SMARTCOURSE_FULL_SIZE_LOAD_W", str: ["EN":"FULL SIZE LOAD", "KR":"다량건조"] ],
    117: [val: "@WM_KR_DR_SMARTCOURSE_DENIM_W", str: ["EN":"DENIM", "KR":"청바지"] ]
]

metadata {
	definition (name: "LG Dryer", namespace: "fison67", author: "fison67") {
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
        /*
        valueTile("curState_label", "", decoration: "flat", width: 3, height: 1) {
            state "default", label:'State'
        }
        valueTile("curState", "device.curState", decoration: "flat", width: 3, height: 1) {
            state "default", label:'${currentValue}'
        }
        */
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
	log.debug "Update >> ${data.key} >> ${data.data}"
    
    def jsonObj = new JsonSlurper().parseText(data.data)
    
    if(jsonObj.State != null){
    	if(jsonObj.State.value as int == 0){
        	sendEvent(name:"switch", value: "off")
        }else{
        	sendEvent(name:"switch", value: "on")
        }
        sendEvent(name:"curState", value: STATE_VALUE[jsonObj.State.value as int]["str"][language])
    }
    
    if(jsonObj.ProcessState != null){
    	def value = PROCESS_STATE_VALUE[jsonObj.ProcessState.value as int]["str"][language]
        if(jsonObj.State.value as int == 0){
        	value = "OFF"
        }
    	sendEvent(name:"processState", value: value)
    }
    
    if(jsonObj.Course != null){
    	def name = "unknown"
    	def item = COURSE_VALUE[jsonObj.Course.value as int]
        if(item){
        	name = COURSE_VALUE[jsonObj.Course.value as int]["str"][language]
        }
    	sendEvent(name:"course", value: name)
    }
    
    if(jsonObj.Remain_Time_H != null){
    	state.remainTimeH = changeTime(jsonObj.Remain_Time_H.rValue)
	}
    if(jsonObj.Remain_Time_M != null){
    	state.remainTimeM = changeTime(jsonObj.Remain_Time_M.rValue)
	}
   
    sendEvent(name:"leftTime", value: state.remainTimeH + ":" + state.remainTimeM + ":00")
    if(jsonObj.Remain_Time_H != null){
    	sendEvent(name:"leftMinute", value: jsonObj.Remain_Time_H.value * 60 + jsonObj.Remain_Time_M.value)
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
        	"HOST": state.app_url,
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
