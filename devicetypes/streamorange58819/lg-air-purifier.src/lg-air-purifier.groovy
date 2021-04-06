/**
 *  LG Air PuriFier(v.0.0.2)
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
OPERATION_VALUE = [
    0: [val: "@AC_MAIN_OPERATION_MODE_COOL_W", str: ["EN": "Cool", "KR": "Cool"]],
	1: [val: "@AC_MAIN_OPERATION_MODE_DRY_W", str: ["EN": "Dry", "KR": "Dry"]],
    2: [val: "@AC_MAIN_OPERATION_MODE_FAN_W", str: ["EN": "Fan", "KR": "Fan"]],
    3: [val: "@AC_MAIN_OPERATION_MODE_AI_W", str: ["EN": "AI", "KR": "AI"]],
    4: [val: "@AC_MAIN_OPERATION_MODE_HEAT_W", str: ["EN": "Heat", "KR": "Heat"]],
    5: [val: "@AC_MAIN_OPERATION_MODE_AIRCLEAN_W", str: ["EN": "Air Clean", "KR": "공기청정"]],
    6: [val: "@AC_MAIN_OPERATION_MODE_ACO_W", str: ["EN": "Aco", "KR": "Aco"]],
    7: [val: "@AC_MAIN_OPERATION_MODE_AROMA_W", str: ["EN": "Aroma", "KR": "아로마"]],
    8: [val: "@AC_MAIN_OPERATION_MODE_ENERGY_SAVING_W", str: ["EN": "Energy Saving", "KR": "Energy saving"]],
    9: [val: "@AP_MAIN_MID_OPMODE_CLEAN_W", str: ["EN": "Clean", "KR": "청정"]],
    10: [val: "@AP_MAIN_MID_OPMODE_SLEEP_W", str: ["EN": "Sleep", "KR": "취침"]],
    11: [val: "@AP_MAIN_MID_OPMODE_SILENT_W", str: ["EN": "Silent", "KR": "정음청정"]],
    12: [val: "@AP_MAIN_MID_OPMODE_HUMIDITY_W", str: ["EN": "Humidity", "KR": "가습"]],
    13: [val: "@AP_MAIN_MID_OPMODE_CIRCULATOR_CLEAN_W", str: ["EN": "Clean Booster", "KR": "클린부스터"]],
    14: [val: "@AP_MAIN_MID_OPMODE_BABY_CARE_W", str: ["EN": "Baby Care", "KR": "싱글청정"]],
    15: [val: "@AP_MAIN_MID_OPMODE_DUAL_CLEAN_W", str: ["EN": "Dual Clean", "KR": "듀얼청정"]],
    16: [val: "@AP_MAIN_MID_OPMODE_AUTO_W", str: ["EN": "Auto", "KR": "오토모드"]]
]

@Field 
CIRCULATOR_VALUE = [
    0: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_LOWST_LOW_W", str: ["EN": "Lowst Low", "KR": "약약약"]],
    1: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_LOWST_W", str: ["EN": "Lowst", "KR": "약약"]],
    2: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_LOW_W", str: ["EN": "Low", "KR": "약"]],
    3: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_LOW_MID_W", str: ["EN": "Low Mid", "KR": "약중"]],
    4: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_MID_W", str: ["EN": "Mid", "KR": "중"]],
    5: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_MID_HIGH_W", str: ["EN": "Mid High", "KR": "중상"]],
    6: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_HIGH_W", str: ["EN": "High", "KR": "상"]],
    7: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_POWER_W", str: ["EN": "Power", "KR": "파워"]],
    8: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_AUTO_W", str: ["EN": "Auto", "KR": "오토"]],
    9: [val:"@AP_MAIN_MID_CIRCULATORSTRENGTH_LINK_W", str: ["EN": "Link", "KR": "Link"]]
]

@Field 
WIND_VALUE = [
    0: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOWST_LOW_W", str: ["EN": "Lowst Low", "KR": "약약약"]],
    1: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOWST_W", str: ["EN": "Lowst", "KR": "약약"]],
    2: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOW_W", str: ["EN": "Low", "KR": "약"]],
    3: [val:"@AP_MAIN_MID_WINDSTRENGTH_LOW_MID_W", str: ["EN": "Low Mid", "KR": "약중"]],
    4: [val:"@AP_MAIN_MID_WINDSTRENGTH_MID_W", str: ["EN": "Mid", "KR": "중"]],
    5: [val:"@AP_MAIN_MID_WINDSTRENGTH_MID_HIGH_W", str: ["EN": "Mid High", "KR": "중상"]],
    6: [val:"@AP_MAIN_MID_WINDSTRENGTH_HIGH_W", str: ["EN": "High", "KR": "상"]],
    7: [val:"@AP_MAIN_MID_WINDSTRENGTH_POWER_W", str: ["EN": "Power", "KR": "파워"]],
    8: [val:"@AP_MAIN_MID_WINDSTRENGTH_AUTO_W", str: ["EN": "Auto", "KR": "오토"]],
    9: [val:"@AP_MAIN_MID_WINDSTRENGTH_LONGPOWWER_W", str: ["EN": "Long POwer", "KR": "중파워"]],
    10: [val:"@AP_MAIN_MID_WINDSTRENGTH_SHOWER_W", str: ["EN": "Shower", "KR": "Shower"]],
    11: [val:"@AP_MAIN_MID_WINDSTRENGTH_FOREST_W", str: ["EN": "Forest", "KR": "Forest"]],
    12: [val:"@AP_MAIN_MID_WINDSTRENGTH_TURBO_W", str: ["EN": "Turbo", "KR": "터보"]],
    13: [val:"@AP_MAIN_MID_WINDSTRENGTH_FASTWIND_W", str: ["EN": "Fast Wind", "KR": "Fast Wind"]]
]

metadata {
	definition (name: "LG Air PuriFier", namespace: "streamorange58819", author: "fison67", mnmn:"fison67", vid: "35542fbd-0129-39aa-9793-82ec4716c216", ocfDeviceType:"oic.d.airpurifier") {
        capability "Switch"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"
        capability "streamorange58819.amode"
        capability "Very Fine Dust Sensor"
        capability "Dust Sensor"
        capability "Fan Speed"
        capability "Refresh"
        
        attribute "circulator", "string"
        attribute "airRemoval", "string"
        attribute "circulatorDirection", "string"
        attribute "totalAirPolution", "number"
        
        command "airRemovalOff"
        command "airRemovalOn"
        command "circulatorDirectionOff"
        command "circulatorDirectionOn"
        
        command "setCir", ["number"]
        command "cur1"
        command "cur2"
        command "cur3"
        command "cur4"
        command "cur5"
	}
    
	preferences {
        input name: "wind1", title:"Wind1" , type: "enum", required: true, options: ["lowest low", "lowest", "low", "low mid", "mid", "mid high", "high", "power", "auto", "long power", "shower", "forest", "turbo", "fast wind"]
        input name: "wind2", title:"Wind2" , type: "enum", required: true, options: ["lowest low", "lowest", "low", "low mid", "mid", "mid high", "high", "power", "auto", "long power", "shower", "forest", "turbo", "fast wind"]
        input name: "wind3", title:"Wind3" , type: "enum", required: true, options: ["lowest low", "lowest", "low", "low mid", "mid", "mid high", "high", "power", "auto", "long power", "shower", "forest", "turbo", "fast wind"]
        input name: "wind4", title:"Wind4" , type: "enum", required: true, options: ["lowest low", "lowest", "low", "low mid", "mid", "mid high", "high", "power", "auto", "long power", "shower", "forest", "turbo", "fast wind"]
        
        input name: "cir1", title:"Circulate#1 Type" , type: "number", required: false, defaultValue: 3
        input name: "cir2", title:"Circulate#2 Type" , type: "number", required: false, defaultValue: 5
        input name: "cir3", title:"Circulate#3 Type" , type: "number", required: false, defaultValue: 7
        input name: "cir4", title:"Circulate#4 Type" , type: "number", required: false, defaultValue: 8
        input name: "cir5", title:"Circulate#5 Type" , type: "number", required: false, defaultValue: 9
        
	}
}

def installed(){
    sendEvent(name:"supportedAmodes", value: ["clean", "humidity"])
    sendEvent(name:"fanSpeed", value: 0)
}

def getModeStr(data){
	if(data == 0){
    	return "off"
    }else if(data == 1){
    	return "dry"
    }else if(data == 2){
    	return "fan"
    }else if(data == 3){
    	return "ai"
    }else if(data == 4){
    	return "heat"
    }else if(data == 5){
    	return "air clean"
    }else if(data == 6){
    	return "aco"
    }else if(data == 7){
    	return "aroma"
    }else if(data == 8){
    	return "energy saving"
    }else if(data == 9){
    	return "clean"
    }else if(data == 10){
    	return "sleep"
    }else if(data == 11){
    	return "silent"
    }else if(data == 12){
    	return "humidity"
    }else if(data == 13){
    	return "clean booster"
    }else if(data == 14){
    	return "baby care"
    }else if(data == 15){
    	return "dual clean"
    }else if(data == 16){
    	return "auto"
    }else if(data == 29){
    	return "humidity"
    }
}

def getWindInt(data){
	if(data == "lowest low"){
    	return 0
    }else if(data == "lowest"){
    	return 1
    }else if(data == "low"){
    	return 2
    }else if(data == "low mid"){
    	return 3
    }else if(data == "mid"){
    	return 4
    }else if(data == "mid high"){
    	return 5
    }else if(data == "high"){
    	return 6
    }else if(data == "power"){
    	return 7
    }else if(data == "auto"){
    	return 8
    }else if(data == "long power"){
    	return 9
    }else if(data == "shower"){
    	return 10
    }else if(data == "forest"){
    	return 11
    }else if(data == "turbo"){
    	return 12
    }else if(data == "fast wind"){
    	return 13
    }else if(data == "fast wind"){
    	return 29
    }
}

def getModeInt(data){
	if(data == "off"){
    	return 0
    }else if(data == "dry"){
    	return 1
    }else if(data == "fan"){
    	return 2
    }else if(data == "ai"){
    	return 3
    }else if(data == "heat"){
    	return 4
    }else if(data == "air clean"){
    	return 5
    }else if(data == "aco"){
    	return 6
    }else if(data == "aroma"){
    	return 7
    }else if(data == "energy saving"){
    	return 8
    }else if(data == "clean"){
    	return 9
    }else if(data == "sleep"){
    	return 10
    }else if(data == "silent"){
    	return 11
    }else if(data == "humidity"){
    	return 12
    }else if(data == "clean booster"){
    	return 13
    }else if(data == "baby care"){
    	return 14
    }else if(data == "dual clean"){
    	return 15
    }else if(data == "auto"){
    	return 16
    }
}

def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
	log.debug "updated"
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
    
    if(jsonObj.Operation){
    	sendEvent(name: "switch", value: jsonObj.Operation.value == "0" ? "off" : "on")

        if(jsonObj.SensorPM10){
            sendEvent(name: "dustLevel", value: jsonObj.SensorPM10.value as int, unit:"μg/m^3")
        }
        if(jsonObj.SensorPM2){
            sendEvent(name: "fineDustLevel", value: jsonObj.SensorPM2.value as int, unit:"μg/m^3")
        }
        if(jsonObj.SensorPM1){
            sendEvent(name: "veryFineDustLevel", value: jsonObj.SensorPM1.value as int, unit:"μg/m^3")
        }

    }
    if(jsonObj.TempCur){
    	sendEvent(name: "temperature", value: jsonObj.TempCur.value as int, unit: "C")
    }
    if(jsonObj.SensorHumidity){
    	sendEvent(name:"humidity", value: jsonObj.SensorHumidity.value as int, unit: "%")
    }
    if(jsonObj.OpMode){
    	sendEvent(name: "amode", value: getModeStr(jsonObj.OpMode.value as int))
    }
    
    if(jsonObj.HumidityCfg){
//    	sendEvent(name: "targetHumidity", value: jsonObj.HumidityCfg.value as int)
    }
    if(jsonObj.TotalAirPolution){
    	sendEvent(name: "totalAirPolution", value: jsonObj.TotalAirPolution.value as int)
    }
    if(jsonObj.WindStrength){
    	sendEvent(name: "wind", value: WIND_VALUE[jsonObj.WindStrength.value as int]["str"][language])
    }
    if(jsonObj.CirculateStrength){
    	sendEvent(name: "circulator", value: CIRCULATOR_VALUE[jsonObj.CirculateStrength.value as int]["str"][language])
    }
    if(jsonObj.CirculateDir){
    	sendEvent(name: "circulatorDirection", value: (jsonObj.CirculateDir.value == "0" ? "off" : "on"))
    }
    if(jsonObj.AirRemoval){
    	sendEvent(name: "airRemoval", value: (jsonObj.AirRemoval.value == "0" ? "off" : "on"))
    }
}

def setTargetHumidity(level){
	makeCommand("SetHumidityCfg", '{"HumidityCfg":"' + level + '"}')
}

def on(){
	makeCommand("SetOperationStart", "Start")
}

def off(){
	makeCommand("SetOperationStop", "Stop")
}

def setCir(val){
	makeCommand("SetCirculateStrength", '{"CirculateStrength":"' + val + '"}')
}

def cir1(){
    setCir(cir1)
}

def cir2(){
    setCir(cir2)
}

def cir3(){
    setCir(cir3)
}

def cir4(){
    setCir(cir4)
}

def cir5(){
    setCir(cir5)
}

def setFanSpeed(val){
    makeCommand("SetWindStrength", '{"WindStrength":"' + getWindInt("settings.wind${val}") + '"}')
}

def setAirPurifierMode(val){
	log.debug "setAirPurifierMode: " + getModeInt(val)
	makeCommand("SetOpMode", '{"OpMode":"' + getModeInt(val) + '"}')
}

def airRemovalOn(){
	makeCommand("SetAirRemoval", '{"AirRemoval":"1"}')
}

def airRemovalOff(){
	makeCommand("SetAirRemoval", '{"AirRemoval":"0"}')
}

def circulatorDirectionOn(){
	makeCommand("SetCirculateDir", '{"CirculateDir":"1"}')
}

def circulatorDirectionOff(){
	makeCommand("SetCirculateDir", '{"CirculateDir":"0"}')
}

def control(cmd, value){
	makeCommand(cmd, value)
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
        "path": "/devices/control",
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
