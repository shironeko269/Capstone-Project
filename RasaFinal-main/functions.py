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


mydb = mysql.connector.connect(
    host="127.0.0.1",
    port=3306,
    user="root",
    password="12345678",
    database="project_football_pitch")

list_name_pitch = list()

list_location = list()

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
        f.write(" - "+name+"\n")
        f.write(" - "+convertkhongdau(name)+"\n")
        list_location.append(convertkhongdau(name).lower())

    sql = "SELECT district_name FROM project_football_pitch.districts;"
    cur.execute(sql)
    for table in cur:
        name = table[0]
        f.write(" - "+name+"\n")
        f.write(" - "+convertkhongdau(name)+"\n")
        list_location.append(convertkhongdau(name).lower())

    f.close()

    print(list_location)

    return []

# create_data_location()
sanbong = "My Ddình"

khuvuc ="Liên Chiểu"

if convertkhongdau(khuvuc.lower()) not in list_location:
    print("chúng tôi không có sân bóng này")
else: 
    print("Đây là link sân")


def test():

    location = "hoa hiep bac"
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
            ++exist
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
                                        "url": ""f"/booking/pitch/{id}",
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
            print("Xin lỗi chúng tôi không nhận diện được khu vực mà bạn muốn tìm, hãy nhập tên quận hoặc phường trong thành phố Đà Nẵng!")
    elif exist == 0:
            print("Rất tiếc không có sân nào trong khu vực mà bạn muốn tìm cả")
    else:
            print("Các sân gần khu vực bạn tìm")
            print(attachment=message)


# test()

def test2():

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
                                    "image_url": f"/images/image-subpitch/+{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": f"/user/booking/sub-pitch/{id}",
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
            print("Xin lỗi hiện tại không có sân nào trống cả!")
        else:
            print("Đây là danh sách các sân nhỏ đang trống")
            print(message)


        return []


def test3():
        list_product = ["giày","áo","quần","áo thun", "bộ quần áo", "găng đấm boxing", "bình lắc", "bóng"]

        product = "áo"

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
                                        "url": f"/product-detail/value={id}/category={id1}",
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
            print("Xin lỗi chúng tôi không bán loại sản phẩm mà bạn tìm! Đây là các sản phẩm đang được bày bán /products")
        elif exist == 0 :
            print("Không có sản phẩm bạn tìm")
        else:
            print("Đây là danh sách các sản phẩm mà chúng tôi đang bán")
            print(message)

        return []




def test4():

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
                                    "image_url": "/images/image-product/{image}",
                                    "buttons": [ 
                                        {
                                        "title": "Xem chi tiết",
                                        "url": ""f"/product-detail/value={id}/category={id1}",
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

        print("Đây là danh sách các sản phẩm bán chạy của cửa hàng")
        
        print(message)

        return []

test4()