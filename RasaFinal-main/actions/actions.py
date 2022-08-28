from typing import Any, Text, Dict, List
from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher
from rasa_sdk import Tracker, FormValidationAction, Action
from rasa_sdk.events import EventType
from rasa_sdk.types import DomainDict
from rasa_sdk.events import SlotSet
from datetime import date, datetime, timedelta
import mysql.connector
from sqlalchemy import null
from weather import Weather
from googletrans import Translator
import re
from rasa_sdk.events import UserUtteranceReverted




# link_web = "https://project-football-pitch.herokuapp.com"
link_web = "http://localhost:8080"

translator = Translator()

mydb = mysql.connector.connect(
    host="127.0.0.1",
    port=3306,
    user="root",
    password="12345678",
    database="project_football_pitch")

list_name_pitch = list()

list_location = list()

list_product = ["giày","áo","quần","áo thun", "bộ quần áo", "găng đấm boxing", "bình lắc"]

hom_nay = ["hôm nay", "bữa nay", "ngày nay", "nay"]
ngay_mai = ["ngày mai", "mai"]
ngay_mot = ["ngày mốt", "mốt"]

patterns = {
    '[àáảãạăắằẵặẳâầấậẫẩ]': 'a',
    '[đ]': 'd',
    '[èéẻẽẹêềếểễệ]': 'e',
    '[ìíỉĩị]': 'i',
    '[òóỏõọôồốổỗộơờớởỡợ]': 'o',
    '[ùúủũụưừứửữự]': 'u',
    '[ỳýỷỹỵ]': 'y'
}

def convertkhongdau(text):
    output = text
    for regex, replace in patterns.items():
        output = re.sub(regex, replace, output)
        # deal with upper case
        output = re.sub(regex.upper(), replace.upper(), output)
    return output


def create_data_pitch_name():

    cur = mydb.cursor()
    sql = "SELECT name FROM project_football_pitch.football_pitchs;"
    cur.execute(sql)

    f = open("data/nameofpitch.txt", "a", encoding='utf-8')
    f.seek(0) 
    f.truncate() 
    list_name_pitch.clear()

    for table in cur:
        name = table[0]
        f.write(name+"\n")
        f.write(convertkhongdau(name).lower()+"\n")
        f.write(name.lower()+"\n")
        list_name_pitch.append(convertkhongdau(name).lower())

    f.close()

    # print(list_name_pitch)
    return []

def create_data_location():

    cur = mydb.cursor()
    
    f = open("data/nameoflocation.txt", "a", encoding='utf-8')
    f.seek(0) 
    f.truncate() 
    list_location.clear()

    sql = "SELECT ward_name FROM project_football_pitch.wards;"
    cur.execute(sql)
    for table in cur:
        name = table[0]
        f.write(name+"\n")
        f.write(convertkhongdau(name).lower()+"\n")
        f.write(name.lower()+"\n")
        list_location.append(convertkhongdau(name).lower())

    sql = "SELECT district_name FROM project_football_pitch.districts;"
    cur.execute(sql)
    for table in cur:
        name = table[0]
        f.write(name+"\n")
        f.write(convertkhongdau(name).lower()+"\n")
        f.write(name.lower()+"\n")
        list_location.append(convertkhongdau(name).lower())

    f.close()

    # print(list_name_pitch)
    return []

ACTION_DEFAULT_FALLBACK_NAME = "ACTION_DEFAULT_FALLBACK_NAME"
class ActionDefaultFallback(Action):
    """Executes the fallback action and goes back to the previous state
    of the dialogue"""

    def name(self) -> Text:
        return ACTION_DEFAULT_FALLBACK_NAME

    async def run(
        self,
        dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any],
    ) -> List[Dict[Text, Any]]:
        dispatcher.utter_message(template="my_custom_fallback_template")

        # Revert user message which led to fallback.
        return [UserUtteranceReverted()]

class ActionShowLinkPitch(Action):

    def name(self) -> Text:
        return "action_show_link_pitch"
    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: "DomainDict") -> List[Dict[Text, Any]]:

        pitch_name = tracker.get_slot("pitch_name")

        create_data_pitch_name()

        cur = mydb.cursor()
        sql = 'select f.* from football_pitchs f where f.name like "%{0}%" and f.user_id is not null;'.format(pitch_name)
        cur.execute(sql)
        
        for table in cur:
            id = table[0]
            name = table[7]
            image = table[6]
            address = table[8]
   

            message = {
                    "type": "template",
                    "payload":{
                        "template_type": "generic",
                        "elements": 
                        [
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{address}",
                                    "image_url": f"/images/image-pitch/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": ""+link_web+f"/booking/pitch/{id}",
                                        "type": "web_url"
                                        }
                                    ]
                                }
                        ]
                                
                    }
                }
        if  convertkhongdau(pitch_name).lower() not in list_name_pitch :
            dispatcher.utter_message(text="Chúng tôi không có sân bóng tên như vậy cả. Vui lòng nhập đúng tên sân bóng!")
        else:
            dispatcher.utter_message(text="Sân bóng bạn đang tìm đây")
            dispatcher.utter_message(attachment=message)
        return []

class ActionShowLinkPitchInLocation(Action):

    def name(self) -> Text:
        return "action_show_link_pitch_in_location"

    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        location = tracker.get_slot("location")

        create_data_location()

        cur = mydb.cursor()

        sql = 'SELECT f.* FROM football_pitchs f \
                inner join wards w on f.ward_id = w.id \
                inner join districts d on w.district_id=d.id \
                where (d.district_name like "%{0}%" or w.ward_name like "%{0}%" or f.street_number like "%{0}%") and f.user_id is not null;'.format(location)

 
        cur.execute(sql)

        elements_pitches = list()
        exist = 0
        for table in cur:
            exist = +1
            id = table[0]
            image = table[6]
            name = table[7]
            address = table[8]
            elements_pitches.append(                                
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{address}",
                                    "image_url": f"/images/image-pitch/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": ""+link_web+f"/booking/pitch/{id}",
                                        "type": "web_url"
                                        }                                                                             
                                    ]
                                })  

        message = {
                    "type": "template",
                    "payload":
                        {
                        "template_type": "generic",
                        "elements": 
                              
                            elements_pitches
                             
                        }
                    }    


        if  convertkhongdau(location).lower() not in list_location :
            dispatcher.utter_message(text="Xin lỗi chúng tôi không nhận diện được khu vực mà bạn muốn tìm, hãy nhập tên quận hoặc phường trong thành phố Đà Nẵng!")
        elif exist == 0:
            dispatcher.utter_message(text=f"Rất tiếc không có sân nào trong khu vực mà bạn muốn tìm cả")
        else:
            dispatcher.utter_message(text=f"Các sân gần khu vực bạn tìm")
            dispatcher.utter_message(attachment=message)

        return []

class ActionShowLinkPitchHasNotBeenBooked(Action):

    def name(self) -> Text:
        return "action_show_link_pitch_has_not_been_booked"

    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        thoi_gian_ngay = datetime.now().strftime('%Y-%m-%d')

        now = datetime.now()

        thoi_gian_gio = now.strftime("%H:%M:%S")
        
        cur = mydb.cursor()


        sql = "select * from sub_pitch s \
            inner join football_pitchs f on f.id = s.pitch_pitch_id \
            inner join wards w on f.ward_id = w.id \
            inner join districts d on w.district_id=d.id \
            where s.id not in \
            (select sp.id as asd from sub_pitch sp inner join football_pitchs_schedule fps \
            on fps.sub_pitch_id = sp.id where ( fps.date_booking = '"+thoi_gian_ngay+"' and '"+thoi_gian_gio+"' between fps.time_start and fps.time_end )) \
            or not exists (select * from football_pitchs_schedule fps where fps.sub_pitch_id = s.id )"

 
        cur.execute(sql)

        elements_san_bong = list()

        exist = 0
        for table in cur:
            exist = +1
            id = table[0]
            image = table[6]
            name = table[11]
            address = table[23]

            elements_san_bong.append(                                
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{address}",
                                    "image_url": f"/images/image-subpitch/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": link_web+f"/user/booking/sub-pitch/{id}",
                                        "type": "web_url"
                                        }                                                                             
                                    ]
                                })  
   

        message = {
                    "type": "template",
                    "payload":
                        {
                        "template_type": "generic",
                        "elements": 
                            elements_san_bong                              
                        }
                }

        if  exist == 0 :
            dispatcher.utter_message(text="Xin lỗi hiện tại không có sân nào trống cả!")
        else:
            dispatcher.utter_message(text=f"Đây là danh sách các sân nhỏ đang trống")
            dispatcher.utter_message(attachment=message)


        return []


class ActionShowLinkPitchHasNotBeenBookedWithEntity(Action):

    def name(self) -> Text:
        return "action_show_link_pitch_has_not_been_booked_with_entity"

    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        location = tracker.get_slot("location")

        create_data_location()
        
        thoi_gian_ngay = datetime.now().strftime('%Y-%m-%d')

        now = datetime.now()

        thoi_gian_gio = now.strftime("%H:%M:%S")
        
        cur = mydb.cursor()

        sql = "select * from sub_pitch s inner join football_pitchs f on f.id = s.pitch_pitch_id \
            inner join wards w on f.ward_id = w.id inner join districts d on w.district_id=d.id \
                 where ((d.district_name like '%"+location+"%' or w.ward_name like '%"+location+"%' \
                or f.street_number like '%"+location+"%') and f.user_id is not null) and (s.id not in \
                (select sp.id as asd from sub_pitch sp inner join football_pitchs_schedule fps on fps.sub_pitch_id = sp.id \
                where ( fps.date_booking = '"+thoi_gian_ngay+"' and '"+thoi_gian_gio+"' between fps.time_start and fps.time_end )) or \
                     not exists (select * from football_pitchs_schedule fps where fps.sub_pitch_id = s.id ));".format(location)

 
        cur.execute(sql)
       

        elements_san_bong = list()

        exist = 0
        for table in cur:
            id = table[0]
            exist = +1
            name = table[11]
            image = table[6]
            address = table[23]

            elements_san_bong.append(                                
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{address}",
                                    "image_url": f"/images/image-subpitch/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": link_web+f"/user/booking/sub-pitch/{id}",
                                        "type": "web_url"
                                        }                                                                             
                                    ]
                                })  
 

        message = {
                    "type": "template",
                    "payload":
                    {
                        "template_type": "generic",
                        "elements": 

                                elements_san_bong  
                    }
                }

        if  convertkhongdau(location).lower() not in list_location :
            dispatcher.utter_message(text="Xin lỗi chúng tôi không nhận diện được khu vực mà bạn muốn tìm, hãy nhập tên quận hoặc phường trong thành phố Đà Nẵng!")
        elif  exist == 0 :
            dispatcher.utter_message(text="Xin lỗi hiện tại không có sân nào trống cả!")
        else:
            dispatcher.utter_message(text=f"Đây là danh sách các sân nhỏ đang trống trong khu vực bạn muốn tìm")
            dispatcher.utter_message(attachment=message)

        return []

class ActionShowLinkProductByCategory(Action):

    def name(self) -> Text:
        return "action_show_link_product_by_category"

    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        product = tracker.get_slot("product")

        cur = mydb.cursor()
        sql = 'select p.* from products p where p.name like "%{0} %" and p.quantity > 0 ;'.format(product)
        cur.execute(sql)

        elements_san_pham=list()
        exist = 0
        for table in cur:
            exist = +1
            id = table[0]
            id1 = table[13]
            name = table[8]
            price = table[9]
            image = table[7]
            elements_san_pham.append(                                
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{price}"" VNĐ",
                                    "image_url": f"/images/image-product/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": ""+link_web+f"/product-detail/value={id}/category={id1}",
                                        "type": "web_url"
                                        }                                                                             
                                    ]
                                })  

        message = {
                    "type": "template",
                    "payload":
                        {
                        "template_type": "generic",
                        "elements": 
                            elements_san_pham                             
                        }
                    }    

        if  product not in list_product :
            dispatcher.utter_message(text=f"Xin lỗi chúng tôi không bán loại sản phẩm mà bạn tìm! Đây là các sản phẩm đang được bày bán "+link_web+"/products")
        elif exist == 0:
            dispatcher.utter_message("Không có sản phẩm bạn tìm")
        else:
            dispatcher.utter_message(text=f"Đây là danh sách các sản phẩm {product} mà chúng tôi đang bán")
            dispatcher.utter_message(attachment=message)

        return []

class ActionShowLinkProductBestSeller(Action):

    def name(self) -> Text:
        return "action_show_link_product_best_seller"

    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        cur = mydb.cursor()
        sql = 'select p.*,sum(o.quantity) as tongSoLuongBan from products p inner join order_detail \
            as o on o.product_id = p.id group by p.id order by tongSoLuongBan desc limit 5;'
        cur.execute(sql)

        elements_san_pham=list()

        for table in cur:
            id = table[0]
            id1 = table[13]
            name = table[8]
            price = table[9]
            image = table[7]
            elements_san_pham.append(                                
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{price}"" VNĐ",
                                    "image_url": f"/images/image-product/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": ""+link_web+f"/product-detail/value={id}/category={id1}",
                                        "type": "web_url"
                                        }                                                                             
                                    ]
                                })  

        message = {
                    "type": "template",
                    "payload":
                        {
                        "template_type": "generic",
                        "elements": 

                            elements_san_pham
                             
                        }
                    }    

        dispatcher.utter_message(text=f"Đây là danh sách các sản phẩm bán chạy của cửa hàng")
        
        dispatcher.utter_message(attachment=message)

        return []


class ActionShowLinkNewProduct(Action):

    def name(self) -> Text:
        return "action_show_link_new_product"

    def run(self, dispatcher: CollectingDispatcher,
        tracker: Tracker,
        domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        cur = mydb.cursor()
        sql = 'SELECT * FROM project_football_pitch.products where (status = 1 and quantity > 0) order by createddate DESC limit 5;'
        cur.execute(sql)

        elements_san_pham=list()

        for table in cur:
            id = table[0]
            id1 = table[13]
            name = table[8]
            price = table[9]
            image = table[7]
            elements_san_pham.append(                                
                                {
                                    "title": f"{name}",
                                    "subtitle": f"{price}"" VNĐ",
                                    "image_url": f"/images/image-product/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": ""+link_web+f"/product-detail/value={id}/category={id1}",
                                        "type": "web_url"
                                        }                                                                             
                                    ]
                                })  

        message = {
                    "type": "template",
                    "payload":
                        {
                        "template_type": "generic",
                        "elements": 

                            elements_san_pham
                             
                        }
                    }    

        dispatcher.utter_message(text=f"Đây là danh sách các sản phẩm mới nhất của cửa hàng")

        dispatcher.utter_message(attachment=message)

        return []

class ActionWeatherForecast(Action):
    def name(self) -> Text:
        return "action_weather"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        thoi_gian_ngay = tracker.get_slot('thoi_gian_ngay')
        buoi_trong_ngay = tracker.get_slot('buoi_trong_ngay')

        thoi_gian = ""
        now = datetime.now()

        if thoi_gian_ngay is None:
            thoi_gian_ngay = "hôm nay"

        if buoi_trong_ngay is None:
            buoi_trong_ngay = "trung bình"
            if (thoi_gian_ngay in hom_nay) | (thoi_gian_ngay is None):
                buoi_trong_ngay = "ở"
                thoi_gian_ngay = "hiện tại"
                thoi_gian = "hiện tại"  
            elif thoi_gian_ngay in ngay_mai:
                thoi_gian_ngay = "ngày mai"
                x = datetime(now.year, now.month, now.day + 1)
                date = x.strftime("%Y-%m-%d")
                thoi_gian = date+" 09:00:00"
            elif thoi_gian_ngay in ngay_mot:
                thoi_gian_ngay = "ngày mốt"
                x = datetime(now.year, now.month, now.day + 2)
                date = x.strftime("%Y-%m-%d")
                thoi_gian = date+" 09:00:00"
        else:
            if thoi_gian_ngay in hom_nay:
                thoi_gian_ngay = "hôm nay"
                date = now.strftime("%Y-%m-%d")
            if thoi_gian_ngay in ngay_mai:
                thoi_gian_ngay = "ngày mai"
                x = datetime(now.year, now.month, now.day + 1)
                date = x.strftime("%Y-%m-%d")
            if thoi_gian_ngay in ngay_mot:
                thoi_gian_ngay = "ngày mốt"
                x = datetime(now.year, now.month, now.day + 2)
                date = x.strftime("%Y-%m-%d")

            if buoi_trong_ngay == "sáng":
                thoi_gian = date+" 06:00:00"
            if buoi_trong_ngay == "trưa":
                thoi_gian = date+" 12:00:00"
            if buoi_trong_ngay == "chiều":
                thoi_gian = date+" 15:00:00"
            if buoi_trong_ngay == "tối":
                thoi_gian = date+" 18:00:00"

        open_msg = Weather(thoi_gian)

        if open_msg == 0:
            response=f"Thời tiết {buoi_trong_ngay} {thoi_gian_ngay} không có. Tôi chỉ dự đoán thời tiết các buổi từ bây giờ đến ngày mốt. Bạn muốn biết thời tiết vào lúc nào?"
        else:
            rain_fc = open_msg['weather'][0]["main"]
            description = open_msg['weather'][0]['description']
            temp = round(open_msg['main']['temp']-273)

            description_vi = translator.translate(description, dest='vi', src='en')

            if rain_fc == "Rain":
                rain_forecast = "có mưa"
            else:
                rain_forecast = "không mưa"

          
            response=f"Thời tiết {buoi_trong_ngay} {thoi_gian_ngay} sẽ {rain_forecast}, {description_vi.text} và có nhiệt độ là {temp} độ C."
    
        dispatcher.utter_message(response)
        return [SlotSet('buoi_trong_ngay', None),
                 SlotSet('thoi_gian_ngay', None)]