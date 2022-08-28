import requests


def Weather(thoi_gian):
    check = 0
    if thoi_gian == "hiện tại":
        api_address='http://api.openweathermap.org/data/2.5/weather?appid=fa456db180b12b94a5a63daa9bf8bdd3&q=danang'
        json_data = requests.get(api_address).json()
        return json_data
    else:
        api_address='http://api.openweathermap.org/data/2.5/forecast?appid=fa456db180b12b94a5a63daa9bf8bdd3&q=danang&cnt=40'
        json_data = requests.get(api_address).json()
        list=json_data['list']
        for data in list:
            if data['dt_txt'] == thoi_gian:
                ++check
                return data
            
        if check == 0:
            return check
   