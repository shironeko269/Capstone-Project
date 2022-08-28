$(document).ready(function () {
    var slider = $("#slider");
    var thumb = $("#thumb");
    var slidesPerPage = 4; //globaly define number of elements per page
    var syncedSecondary = true;
    slider.owlCarousel({
        items: 1,
        slideSpeed: 2000,
        nav: false,
        autoplay: false,
        dots: false,
        loop: true,
        responsiveRefreshRate: 200
    }).on('changed.owl.carousel', syncPosition);
    thumb
        .on('initialized.owl.carousel', function () {
            thumb.find(".owl-item").eq(0).addClass("current");
        })
        .owlCarousel({
            items: slidesPerPage,
            dots: false,
            nav: true,
            item: 4,
            smartSpeed: 200,
            slideSpeed: 500,
            slideBy: slidesPerPage,
            navText: ['<svg width="18px" height="18px" viewBox="0 0 11 20"><path style="fill:none;stroke-width: 1px;stroke: #000;" d="M9.554,1.001l-8.607,8.607l8.607,8.606"/></svg>', '<svg width="25px" height="25px" viewBox="0 0 11 20" version="1.1"><path style="fill:none;stroke-width: 1px;stroke: #000;" d="M1.054,18.214l8.606,-8.606l-8.606,-8.607"/></svg>'],
            responsiveRefreshRate: 100
        }).on('changed.owl.carousel', syncPosition2);

    function syncPosition(el) {
        var count = el.item.count - 1;
        var current = Math.round(el.item.index - (el.item.count / 2) - .5);
        if (current < 0) {
            current = count;
        }
        if (current > count) {
            current = 0;
        }
        thumb
            .find(".owl-item")
            .removeClass("current")
            .eq(current)
            .addClass("current");
        var onscreen = thumb.find('.owl-item.active').length - 1;
        var start = thumb.find('.owl-item.active').first().index();
        var end = thumb.find('.owl-item.active').last().index();
        if (current > end) {
            thumb.data('owl.carousel').to(current, 100, true);
        }
        if (current < start) {
            thumb.data('owl.carousel').to(current - onscreen, 100, true);
        }
    }

    function syncPosition2(el) {
        if (syncedSecondary) {
            var number = el.item.index;
            slider.data('owl.carousel').to(number, 100, true);
        }
    }

    thumb.on("click", ".owl-item", function (e) {
        e.preventDefault();
        var number = $(this).index();
        slider.data('owl.carousel').to(number, 300, true);
    });
    $(".qtyminus").on("click", function () {
        var now = $(".qty").val();
        if ($.isNumeric(now)) {
            if (parseInt(now) - 1 > 0) {
                now--;
            }
            $(".qty").val(now);
        }
    })
    $(".qtyplus").on("click", function () {
        var now = $(".qty").val();
        if ($.isNumeric(now)) {
            $(".qty").val(parseInt(now) + 1);
        }
    });
});

// ----------------------------- JAVASCRIPT ------------------------------------
function thongbao() {
    document.getElementById("thongbao").innerHTML = "Vui lòng kiểm tra email và chuyển khoản theo thông tin chuyển khoản trong mail trước khi nhận sân 1 tiếng.";
}

function thongbao1() {
    document.getElementById("thongbao").innerHTML = "Vui lòng kiểm tra email và đến sân thanh toán tiền cọc trước khi nhận sân 1 tiếng.";

}

function getToday() {
    var today = new Date();
    var date = today.getDate() + '/' + (today.getMonth() + 1) + '/' + today.getFullYear();
    console.log(date);
    return date;
}

function localStringToNumber(id) {
    let text = document.getElementById(`${id}`).innerText;
    let textRemoveCommas = text.replace(/\D/g, '');
    let num = parseInt(textRemoveCommas, 10);
    return num;
}

function setPreOrderFee() {
    let total = localStringToNumber("totalFinal");
    var preOrderFee = 0.3 * total;
    console.log("pre-order:", preOrderFee)
    $("#preOrder").val(preOrderFee);
}

function timeFee() {
    let priceNum = localStringToNumber("price");
    console.log(priceNum);
    var st = document.getElementById("timestart").value;
    var e = document.getElementById("timeend").value;
    if (st === "" || e === "") {
        console.log("Not null");
    } else {
        console.log("Time is: ");
        var t1 = new Date("October 15, 1996 " + st);
        var t2 = new Date("October 15, 1996 " + e);

        let t1_Hour = t1.getHours();
        let t2_Hour = t2.getHours();
        let t1_Mi = t1.getMinutes();
        let t2_Mi = t2.getMinutes();
        if (t2_Hour === 0) {
            t2_Hour = 24;
        }
        let fee = (((t2_Hour * 60) + t2_Mi) - ((t1_Hour * 60) + t1_Mi)) * (priceNum / 60);
        if (fee > 0) {
            document.getElementById("hourFee").innerHTML = Math.round(fee).toLocaleString("en-AU");
            setValueForElement(sumBy2Id('hourFee', 'totalAll').toLocaleString("en-AU"), 'totalFinal'); //Tong cong
            setPreOrderFee();
            checkPreOrderWithTotal();
            return fee;
        } else {
            setError("timestart", "invalid-time-start", "Vui lòng chọn lại giờ bắt đầu");
        }
    }
}

function increaseNum(quantity) {
    let num = parseInt($(`#${quantity}`).val(), 10);
    $(`#${quantity}`).val(num + 1);
}

function setServiceFeeTotal() {
    var x, i, z, sum = 0,
        zText;
    x = document.querySelectorAll("p.tt");
    for (i = 0; i < x.length; i++) {
        zText = x[i].innerText;
        if (zText === '') {
            zText = '0';
        }
        z = parseInt(zText, 10);
        sum += z;
    }
    console.log(parseInt('', 0));
    console.log("sum:" + sum);
    document.getElementById("totalAll").innerHTML = sum.toLocaleString("en-AU");
    return sum;
}

function getFeeOneCard(price, quantity, tt) {
    var x = document.querySelectorAll(`h8#${price}`);
    var y = document.querySelectorAll(`input#${quantity}`);
    var xNum = parseInt(x[0].innerText.replace(/\D/g, ''), 10)
    var yNum = parseInt(y[0].value, 10);
    var totalOne = yNum * xNum;
    document.getElementById(`${tt}`).innerHTML = totalOne;
    setServiceFeeTotal();
    setValueForElement(sumBy2Id('hourFee', 'totalAll').toLocaleString("en-AU"), 'totalFinal'); //Tong cong
    setPreOrderFee();
    console.log(sumBy2Id('hourFee', 'totalAll'));
    checkPreOrderWithTotal();
}

function totalAll() {
    var x, y;
    x = timeFee();
    y = calTotal();
    console.log(Math.round(x + y));
    document.getElementById("totalFinal").innerHTML = (x + y);
}

function sumBy2Id(id1, id2) {
    var x, y, sum;
    x = localStringToNumber(`${id1}`);
    y = localStringToNumber(`${id2}`);
    sum = parseInt(x, 10) + parseInt(y, 10);
    return sum;
}

function subBy2Id(id1, id2) {
    var x, y, sub;
    x = localStringToNumber(`${id1}`);
    y = localStringToNumber(`${id2}`);
    sub = parseInt(x, 10) - parseInt(y, 10);
    return sub;
}

function setValueForElement(value, id) {
    document.getElementById(id).innerHTML = value;
}

function setElementById(id1, id2) {
    document.getElementById(id1).innerHTML = document.getElementById(id2).innerText;
}

function clickCancel() {
    $('#cancel').click();
}

$(function () {
    $('#exampleModal').on('show.bs.modal', function (e) {
        var button = e.relatedTarget;
        if ($(button).hasClass('btn btn-danger mt-3')) {
            e.preventDefault();
            e.relatedTarget
        }
    });
});

function checkValid() {
    let check;
    if ($('.error-message').text() == "khung giờ bạn chọn hợp lệ" && $('#preOrder').css("border-color") == "green") {
        check = true;
        return check;
    } else {
        check = false;
        return check;
    }
}


function setUpModal() {
    // target = $("#btnBooking").attr('data-bs-target');
    if (checkEmptyInput() == 0 && checkPreOrder() == 0 && $('.error-message').text() == "khung giờ bạn chọn hợp lệ") {
        setValueForElement(getToday(), "createDateMd");
        if (document.getElementById("totalAll").innerText !== '0') {
            setUpServiceTable();
        }
        setElementById('serviceFee', 'totalAll');   // Phi dich vu
        setElementById('hourFeeMd', 'hourFee');     // Phi dat san (time)
        setElementById('totalAllMd', 'totalFinal'); // Tong cong
        setElementById('subPitchNameMd', 'subPitchName'); // Ten san
        $("#noteMd").text($("#note").val());
        setElementById('addressMd', 'address'); // Ten san
        setValueForElement(getTimeBooking(), "timeMd");
        setValueForElement(document.getElementById("dateInput").value, "dateMd");
        setPreOrderToModal();                               //Dat truoc
        setValueForElement(subBy2Id('totalAllMd', 'preOrderMd').toLocaleString("en-AU"), 'postOrderMd'); //Phi con lai
        $("#exampleModal").modal('show');
    } else {
        $(function () {
            $('#exampleModal').on('show.bs.modal', function (e) {
                var button = e.relatedTarget;
                if ($(button).hasClass('btn btn-danger mt-3')) {
                    e.preventDefault();
                    e.relatedTarget
                }
            });
        });
    }
}

function setUpServiceTable() {
    $.ajax({
        type: "GET",
        url: "/getListService",
        data: 1,
        success: function (result) {
            let name, quantity, total, i, sum = "", id;
            name = document.querySelectorAll("div.card-body > h5");
            id = document.querySelectorAll("div.card-body > p.serviceId");
            unit = document.querySelectorAll("div.card-body > h7");
            quantity = document.querySelectorAll("div.card-body > input");
            total = document.querySelectorAll("div.card-body > p.tt");
            let da = result.data;
            let price = []
            for (let a = 0; a < quantity.length; a++) {
                price.push(0);
            }
            da.forEach(value => {
                for (let j = 0; j < id.length; j++) {
                    if (quantity[j].value != 0) {
                        console.log(value.id);
                        if (id[j].innerText == value.id) {
                            price[j]=value.price;
                        }
                    }
                }
            });
            console.log("list price:", price);
            for (i = 0; i <= name.length; i++) {
                if (quantity[i].value != 0) {
                    sum += '<tr class="tbody">' +
                        '<td>' + name[i].innerText + '</td>' +
                        '<td style="display: none;">' + id[i].innerText + '</td>' +
                        '<td style="text-align: center;">' + unit[i].innerText + '</td>' +
                        '<td style="text-align: center;">' + price[i] + 'đ' + '</td>' +
                        '<td style="text-align: center;">' + quantity[i].value + '</td>' +
                        '<td style="text-align: center;">' + parseInt(total[i].innerText, 10).toLocaleString("en-AU") + 'đ' + '</td>' +
                        '</tr>';
                }
                if (i == (name.length - 1)) {
                    break;
                } else {
                    continue;
                }
            }
            document.getElementById("serviceTable").innerHTML = sum;
        }
    });
}

function reset() {
    $("#dateInput").val('');
    $("#timestart").val('');
    $("#timeend").val('');
    $("#preOrder").val('');
    $("#hourFee").text('0');
    $("#totalAll").text('0');
    $("#totalFinal").text('0');
    $("#note").text('');
    $(".error-message").text('');
    $('input[type=number]').each(function (i, item) {
        $(`#${item.id}`).val('0');
    });

}

function setPreOrderToModal() {
    document.getElementById("preOrderMd").innerHTML = parseInt($("#preOrder").val(), 10).toLocaleString("en-AU");
}

function getTimeBooking() {
    var st = document.getElementById("timestart").value;
    var e = document.getElementById("timeend").value;
    var str = st + ' -- ' + e;
    console.log("time: " + str);
    return str;
}


function confirmBooking() {
    paymentStart();
}

//                        ========================AJAX=============================
var noSearchResultDiv = '<div  id="noSearchResult_1"  style="text-align: center; height: fit-content;">\n' +
    '                                <h2>Tìm kiếm không có kết quả</h2>\n' +
    '                                <p style="padding-top: 2%;color: #0D0A0A; font-weight: 400;">Xin lỗi, chúng tôi không thể tìm được kết quả hợp với tìm kiếm của bạn</p>\n' +
    '                                <i class="fas fa-search fa-4x"></i>\n' +
    '                            </div>';

function cbb1change() {
    var cbb1 = document.getElementById("cbb1");
    // console.log("Quận: "cbb1)
    var cbb2 = document.getElementById("cbb2");
    var cbb1options = cbb1.options;
    for (var i = 0; i < cbb1options.length; i++) {
        var option = cbb1options[i];
        if (option.selected == false) {
            var classHide = option.value;
            var optionsHide = document.getElementsByClassName("" + classHide + "");
            for (var idx2 = 0; idx2 < optionsHide.length; idx2++) {
                var optionHide = optionsHide[idx2];
                optionHide.selected = false;
                optionHide.style.display = "none";
            }
        } else {
            cbb2.disabled = false;
            var classShow = option.value;
            var optionsShow = document.getElementsByClassName("" + classShow + "");
            for (var idx2 = 0; idx2 < optionsShow.length; idx2++) {
                var optionShow = optionsShow[idx2];
                optionShow.style.display = "block";
            }
        }
    }
    const app = document.getElementById("cbb1").value;
    $.ajax({
        type: "GET",
        url: "/findPitchByDistrict",
        data: {app},
        success: function (result) {
            $('#allPitch').empty();
            $('#noResultOption').empty();
            $('#allPitchAlert').text("Danh sách tìm kiếm");

            $('#searchByKeywordTitle').empty();
            $('#noSearchResult').empty();
            $('.content__paging').empty();
            $('#footer').empty();
            $('.inner-content').empty();

            if (result.status == "success") {
                $("#allPitch").css("height", "fit-content");
                $.each(result.data,
                    function (i, item) {
                        var info =
                            '<div class="col-sm-4 p-3 pb-3">' +
                            '<div class="card p-2"  style="height: 400px; width: 360px; border-radius: 1em;" >\n' +
                            '                                <div class="">\n' +
                            '                                    <a href="">\n' +
                            '                                        <img src="/images/image-pitch/' + item.image + '"style="width: 338px; height: 210px; margin-top: 5px">\n' +
                            '                                    </a>\n' +
                            '                                    <div style="padding: 5px">\n' +
                            '                                        <h6  style="margin-bottom: 0;color: #1a6692; margin-top: 5px">' + item.name + '</h6>\n' +
                            '                                        <div>\n' +
                            '                                            <span style="float: left"><i class="fas fa-map-marker-alt"style="margin-top: 8px;"></i>&nbsp;</span>\n' +
                            '                                            <p style="padding-top: 2%;color: #0D0A0A; font-weight: 400;"\n' +
                            '                                               >' + item.streetNumber + ' - ' + item.wardName + ' - ' + item.districtName + ' - TP Đà Nẵng' +
                            '                                            </p>\n' +
                            '                                        </div>\n' +
                            '\n' +
                            '                                        <a href="/booking/pitch/' + item.id + '">\n' +
                            '                                            <button type="button" class="btn btn-outline-danger col-12">Xem thêm\n' +
                            '                                            </button>\n' +
                            '                                        </a>\n' +
                            '                                    </div>\n' +
                            '                                </div>\n' +
                            '                            </div>' +
                            '                            </div>';
                        console.log(result.data);
                        $('#allPitch').append(info)
                        $('.inner-content').empty();

                    });
                if ($('#allPitch').text() === '') {
                    $("#allPitch").css("height", "fit-content");
                    $('#noResultOption').html(noSearchResultDiv);
                }
            } else {
                console.log("Fail: ", result);
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function findPitchByWardOption() {
    const app = document.getElementById("cbb2").value;
    const app1 = document.getElementById("cbb1").value;
    $.ajax({
        type: "GET",
        url: "/findPitchByWard",
        data: {app, app1},
        success: function (result) {
            $('#allPitch').empty();
            $('#noResultOption').empty();
            $('#allPitchAlert').text("Danh sách tìm kiếm");
            $('#noSearchResult').empty();
            $('#searchByKeywordTitle').empty();
            $('#footer').empty();
            $('.content__paging').empty();
            if (result.status === "success") {
                $("#allPitch").css("height", "fit-content");
                $.each(result.data,
                    function (i, item) {
                        var info =
                            '<div class="col-sm-4 p-3 pb-3">' +
                            '<div class="card p-2"  style="height: 400px; width: 360px; border-radius: 1em;" >\n' +
                            '                                <div class="">\n' +
                            '                                    <a href="">\n' +
                            '                                        <img src="/images/image-pitch/' + item.image + '"style="width: 338px; height: 210px; margin-top: 5px">\n' +
                            '                                    </a>\n' +
                            '                                    <div style="padding: 5px">\n' +
                            '                                        <h6  style="margin-bottom: 0;color: #1a6692; margin-top: 5px">' + item.name + '</h6>\n' +
                            '                                        <div>\n' +
                            '                                            <span style="float: left"><i class="fas fa-map-marker-alt"style="margin-top: 8px;"></i>&nbsp;</span>\n' +
                            '                                            <p style="padding-top: 2%;color: #0D0A0A; font-weight: 400;"\n' +
                            '                                               >' + item.streetNumber + ' - ' + item.wardName + ' - ' + item.districtName + ' - TP Đà Nẵng' +
                            '                                            </p>\n' +
                            '                                        </div>\n' +
                            '\n' +
                            '                                        <a href="/booking/pitch/' + item.id + '">\n' +
                            '                                            <button type="button" class="btn btn-outline-danger col-12">Xem thêm\n' +
                            '                                            </button>\n' +
                            '                                        </a>\n' +
                            '                                    </div>\n' +
                            '                                </div>\n' +
                            '                            </div>' +
                            '                            </div>';
                        console.log(result.data);
                        $('#allPitch').append(info)
                    });
                if ($('#allPitch').text() === '') {
                    $("#allPitch").css("height", "fit-content");
                    $('#noResultOption').html(noSearchResultDiv);
                }
            } else {
                console.log("Fail: ", result);
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function findSubPitchByPitchType() {
    const pitchType = document.getElementById("pitchType").value;
    const pitchId = document.getElementById("pitchId").value;
    $.ajax({
        type: "GET",
        url: "/findSubPitchByType",
        data: {pitchType, pitchId},
        success: function (result) {
            if (result.status == "success") {
                $('#allSubPitch').css("display", "none");
                $("#searchResultLabel").css("display", "block");
                $('#listSubPitchResult').empty();
                $.each(result.data,
                    function (i, item) {
                        var info =
                            '<div class="col-sm-4 ">\n' +
                            '                       <div class="product-item btn-light">\n' +
                            '                                <a href="">\n' +
                            '                                    <img src="/images/image-subpitch/' + item.image1 + '"\n' +
                            '                                         style="width: 100%; height: 145px">\n' +
                            '                                </a>\n' +
                            '                                <div style="padding: 7px;">\n' +
                            '                                    <p></p>\n' +
                            '                                    <h6 style="color: #1a6692;">' + item.name + '</h6>\n' +
                            '                                    <h7 style="color: #DC3545;">' + item.price + '</h7><span style="color: #DC3545;"> đ/h</span>\n' +
                            '                                    <p style="font-weight: 400"><i class="fas fa-user"></i> ' + item.nameType + '</p>\n' +
                            '                                    <a href="/user/booking/sub-pitch/' + item.id + '">\n' +
                            '                                        <button type="button" class="btn btn-outline-danger col-12">Đặt ngay</button>\n' +
                            '                                    </a>\n' +
                            '                                </div>\n' +
                            '                            </div>\n' +
                            '                        </div>';
                        console.log(result.data);
                        $('#listSubPitchResult').append(info);
                    });
            } else {
                console.log("Fail: ", result);
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}


function save(idPayment) {
    // PREPARE FORM DATA
    var tdArr, dataArr = [];
    var numTr = document.querySelectorAll("tr.tbody").length;
    tdArr = document.querySelectorAll("tr.tbody > td");
    let j = 0;
    for (let i = 0; i < numTr; i++) {
        var Data = {
            name: tdArr[j].innerText,
            id: parseInt(tdArr[j + 1].innerText),
            unit: tdArr[j + 2].innerText,
            price: tdArr[j + 3].innerText.replace(/\D/g, ''),
            quantity: tdArr[j + 4].innerText
        }
        dataArr.push(Data);
        j += 6;
    }
    console.log(Data);
    var bookingInfo = {
        subPitchId: $("#subPitchId").text(),
        price: $("#price").text().replace(/\D/g, ''),
        dateBooking: $("#dateInput").val(),
        timeStart: $("#timestart").val(),
        timeEnd: $("#timeend").val(),
        hourFee: $("#hourFee").text().replace(/\D/g, ''),
        listService: dataArr,
        note: $("#note").val(),
        preOrder: $("#preOrder").val(),
        paymentId: idPayment,
    }
    var json = JSON.stringify(bookingInfo);
    console.log("json", json);
    $.ajax({
        type: "POST",
        url: "/user/save",
        data: json,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (msg) {
            alert('In Ajax');
        }
    });
}

//paypal
const paymentStart = () => {
    console.log("payment started....");
    let amount = $("#preOrder").val();
    console.log(amount);
    if (amount == "" || amount == null) {
        swal("Failed !!", "Amount is required !!", "error");
        return;
    }
    //code
    //call ajax
    $.ajax(
        {
            url: '/user/create_order',
            data: JSON.stringify({amount: amount, info: 'order_request'}),
            contentType: 'application/json',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                console.log(response);
                if (response.status == "created") {
                    // open payment form
                    let options = {
                        key: "rzp_test_SYzQTN6MOUcF9L",
                        amount: response.amount,
                        currency: "INR",
                        name: "Yotsuba Payment",
                        description: "Donation",
                        image: "data:image/jpeg;base64,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",
                        order_id: response.id,
                        handler: function (response) {
                            console.log(response.razorpay_payment_id)
                            console.log(response.razorpay_order_id)
                            console.log(response.razorpay_signature)
                            console.log('payment successful!!')
                            //alert("congrates !! Payment successful !!")
                            updatePaymentOnServer(
                                response.razorpay_payment_id,
                                response.razorpay_order_id,
                                "Đã thanh toán!"
                            );
                            swal("Xác nhận thanh toán!", "Chúc mừng bạn đã đặt sân thành công!!", "success");

                        },
                        "prefill": {
                            "name": "",
                            "email": "",
                            "contact": ""
                        },
                        "notes": {
                            "address": "Razorpay Corporate Office"
                        },
                        "theme": {
                            "color": "#3399cc"
                        },
                    };

                    var rzp = new Razorpay(options);
                    rzp.on('payment.failed', function (response) {
                        console.log(response.error.code);
                        console.log(response.error.description);
                        console.log(response.error.source);
                        console.log(response.error.step);
                        console.log(response.error.reason);
                        console.log(response.error.metadata.order_id);
                        console.log(response.error.metadata.payment_id);
                        //alert("Oops payment failed!")
                        swal("Failed !!", "Oops payment failed !!", "error");
                    });
                    rzp.open();
                }
            },
            error: function (error) {
                console.log(error);
                alert("something went wrong!");
            }
        }
    )
};

function updatePaymentOnServer(payment_id, order_id, status) {
    $.ajax(
        {
            url: '/user/update_order',
            data: JSON.stringify({payment_id: payment_id, order_id: order_id, status: status}),
            contentType: 'application/json',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                // var idPayment= [[${idPayment}]];
                // console.log("id payment:",idPayment)
                swal("Xác nhận thanh toán!", "Chúc mừng bạn đã đặt sân thành công!!", "success");
                setTimeout($('#cancel').click(), 1000);
                console.log(response.idPayment);
                var id_Payment = response.idPayment;
                save(id_Payment);
                reset();
                setTimeout(() =>{
                    window.location.href = "/user/history-invoice-booking";
                },5000)
            },
            error: function (error) {
                swal("Failed !!",
                    "Your payment is successful , but we did not get on server , we will contact you as soon as possible",
                    "error"
                );
            }
        })
}

