// ----------------------------- JAVASCRIPT ------------------------------------
function thongbao(){
    document.getElementById("thongbao").innerHTML="Vui lòng kiểm tra email và chuyển khoản theo thông tin chuyển khoản trong mail trước khi nhận sân 1 tiếng.";
}
function thongbao1(){
    document.getElementById("thongbao").innerHTML="Vui lòng kiểm tra email và đến sân thanh toán tiền cọc trước khi nhận sân 1 tiếng.";

}
function getToday()
{
    var today = new Date();
    var date = today.getDate()+'/'+(today.getMonth()+1)+'/'+today.getFullYear();
    console.log(date);
    return date;
}

function timeFee(){
    var price = document.getElementById("price");
    var priceNum = parseInt(price.innerText, 10);
    console.log(priceNum);
    var st = document.getElementById("timestart").value;
    var e = document.getElementById("timeend").value;
    if (st === "" || e === "") {
        console.log("Not null");
    }else{
        console.log("Time is: ");
        // getTime();
        var t1 = new Date("October 15, 1996 "+st);
        var t2 = new Date("October 15, 1996 "+e);

        let t1_Hour = t1.getHours(); let t2_Hour = t2.getHours();
        let t1_Mi = t1.getMinutes(); let t2_Mi = t2.getMinutes();
        if (t2_Hour===0) {
            t2_Hour=24;
        }
        let fee = (((t2_Hour*60)+t2_Mi) - ((t1_Hour*60)+t1_Mi))*(priceNum/60);
        if (fee>0) {
            document.getElementById("hourFee").innerHTML = Math.round(fee);
            setValueForElement(sumBy2Id('hourFee','totalAll'),'totalFinal'); //Tong cong
            // setUpTable();
            return fee;
        }
    }
}
function setServiceFeeTotal(){
    var x, i, z, sum=0, zText;
    x = document.querySelectorAll("p.tt");
    for (i = 0; i < x.length; i++) {
        zText = x[i].innerText;
        if (zText === '') {
            zText = '0';
        }
        z = parseInt(zText, 10);
        sum+=z;
    }
    console.log(parseInt('', 0));
    console.log("sum:"+sum);
    document.getElementById("totalAll").innerHTML=sum;
    return sum;
}
function getFeeOneCard(price,quantity,tt){
    var x = document.querySelectorAll(`h8#${price}`);
    var y = document.querySelectorAll(`input#${quantity}`);
    var xNum = parseInt(x[0].innerText, 10)
    var yNum = parseInt(y[0].value, 10);

    var totalOne = yNum*xNum;
    document.getElementById(`${tt}`).innerHTML = totalOne;
    setServiceFeeTotal();
    setValueForElement(sumBy2Id('hourFee','totalAll'),'totalFinal'); //Tong cong
}

function totalAll(){
    var x, y;
    x = timeFee();
    y = calTotal();
    console.log(Math.round(x+y));
    document.getElementById("totalFinal").innerHTML=(x+y);
}

function sumBy2Id(id1, id2){
    var x, y, sum;
    x = document.getElementById(id1).innerText;
    y = document.getElementById(id2).innerText;
    sum = parseInt(x, 10)+parseInt(y, 10);
    // console.log("Sum 2 id is: "+sum);
    // document.getElementById("totalFinal").innerHTML = sum;
    return sum;
}
function subBy2Id(id1, id2){
    var x, y, sub;
    x = document.getElementById(id1).innerText;
    y = document.getElementById(id2).innerText;
    sub = parseInt(x, 10)-parseInt(y, 10);
    // console.log("Sum 2 id is: "+sub);
    // document.getElementById("totalFinal").innerHTML = sub;
    return sub;
}
function setValueForElement(value, id){
    document.getElementById(id).innerHTML = value;
}
function setElementById(id1,id2){
    document.getElementById(id1).innerHTML = document.getElementById(id2).innerText;
}

function setUpModal(){
    setValueForElement(getToday(), "createDateMd");
    if (document.getElementById("totalAll").innerText !== '0') {
        setUpServiceTable();
    }
    setElementById('serviceFee','totalAll'); //Phi dich vu
    setElementById('hourFeeMd','hourFee'); //Phi dat san (time)
    setElementById('totalAllMd','totalFinal'); //Tong cong
    // setValueForElement(sumBy2Id('hourFee','totalAll'),'totalFinal');
    setValueForElement(getTimeBooking(), "timeMd");
    setValueForElement(document.getElementById("dateInput").value, "dateMd");
    setPreOrderToModal();   //Dat truoc
    setValueForElement(subBy2Id('totalAllMd','preOrderMd'),'postOrderMd'); //Phi con lai
}

function setUpServiceTable(){
    var name, price ,quantity, total, i, sum;
    name = document.querySelectorAll("div.card-body > h5");
    price = document.querySelectorAll("div.card-body > h8");
    quantity = document.querySelectorAll("div.card-body > input");
    total = document.querySelectorAll("div.card-body > p");
    for (i = 0; i <= name.length; i++) {
        if (quantity[i].value != 0) {
            sum += '<tr><td>'+name[i].innerText+'</td>'+
                '<td style="text-align: center;>2</td>'+
                '<td style="text-align: center;>lon</td>'+
                '<td style="text-align: center;>4</td>'+
                '<td style="text-align: center;>'+price[i].innerText+'</td>'+
                '<td style="text-align: center;>6</td>'+
                '<td style="text-align: center;>'+quantity[i].value+'</td>'+
                '<td style="text-align: center;>8</td>'+
                '<td style="text-align: center;>'+total[i].innerText+'</td>'+
                '</tr>';
            console.log("sum: "+sum.slice(9));
            // document.getElementById("serviceTable").innerHTML += sum;
        }
        if (i==(name.length-2)) {
            // alert("break");
            break;
        }
        else{
            // console.log("null");
            continue;
        }
    }
    document.getElementById("serviceTable").innerHTML = sum.slice(9);
    // setUpTable();
}
function reset(){
    document.getElementById("serviceTable").innerHTML = "";
}
function setPreOrderToModal(){
    document.getElementById("preOrderMd").innerHTML = document.getElementById("preOrder").value;
}
function getTimeBooking(){
    var st = document.getElementById("timestart").value;
    var e = document.getElementById("timeend").value;
    var str = st +' -- '+e;
    console.log("time: "+str);
    return str;
}