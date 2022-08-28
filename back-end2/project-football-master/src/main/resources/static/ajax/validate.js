dateInput.min = new Date().toISOString().slice(0, -14);
var date = document.getElementById("dateInput");
let element = document.getElementById("preOrder");

function setErrorForTimeInput(message) {
    if (message == "giờ bắt đầu bạn chọn trễ hơn giờ kết thúc" || message == "giờ bắt đầu bạn chọn không hợp lệ") {
        setError("timestart", "invalid-time-start", null);
        return;
    }
    if (message == "giờ kết thúc bạn chọn không hợp lệ") {
        setError("timeend", "invalid-time-end", null);
        return;
    }
    if (message == "khung giờ bạn chọn không hợp lệ" ) {
        setError("timestart", "invalid-time-start", null);
        setError("timeend", "invalid-time-end", null);
        return;
    }
    if (message == "khung giờ bạn chọn hợp lệ") {
        $('.error-message').css("color", 'green')
        setSuccess("timestart", "invalid-time-start", null);
        setSuccess("timeend", "invalid-time-end", null);
        return;
    }
}

function setError(id, feedbackId, message) {
    $('.error-message').css("color", 'red')
    $(`#${id}`).css("border-color", 'red');
    $(`.${feedbackId}`).css("color", 'red');
    $(`.${feedbackId}`).text(message);
}

function setSuccess(id, feedbackId) {
    $(`#${id}`).css("border-color", '#2ecc71');
    $(`.${feedbackId}`).text("");
}
document.getElementById("dateInput").scrollIntoView({block: "end"});

function checkPreOrderWithTotal(){
    let totalFinal = localStringToNumber("totalFinal");
    let preOrder = $('#preOrder').val();
    let totalFinalVal = parseInt(totalFinal, 10);
    let preOrderVal = parseInt(preOrder, 10);
    let check=1;
    if (Number.isInteger(preOrderVal)) {
        if (totalFinalVal == 0) {
            setError("preOrder", "invalid-preOder", "bạn chưa chọn thời gian đặt sân");
            check = 1;
            return check;
        }
        if (preOrderVal < (totalFinalVal * 0.3)) {
            setError("preOrder", "invalid-preOder", "số tiền trả trước ít nhất phải bằng 30% tổng cộng");
            check = 1;
            return check;
        }
        if (preOrderVal > totalFinalVal) {
            setError("preOrder", "invalid-preOder", "số tiền trả trước bạn nhập lớn hơn tổng cộng");
            check = 1;
            return check;
        } else {
            setSuccess("preOrder", "invalid-preOder");
            check = 0;
            return check;
        }
    } else {
        check = 0;
        return check;
    }
}


function checkPreOrder() {
    let totalFinal = localStringToNumber("totalFinal");
    let preOrder = $('#preOrder').val();
    let totalFinalVal = parseInt(totalFinal, 10);
    let preOrderVal = parseInt(preOrder, 10);
    console.log("Pre order:",preOrderVal)
    let check = 0;
    if (preOrder === '') {
        setError("preOrder", "invalid-preOder", "vui lòng nhập số tiền trả trước");
        check = 1;
        return check;
    } else {
        if (Number.isInteger(preOrderVal)) {
            if (totalFinalVal == 0) {
                setError("preOrder", "invalid-preOder", "bạn chưa chọn thời gian đặt sân");
                check = 1;
                return check;
            }
            if (preOrderVal < (totalFinalVal * 0.3)) {
                setError("preOrder", "invalid-preOder", "số tiền trả trước ít nhất phải bằng 30% tổng cộng");
                check = 1;
                return check;
            }
            if (preOrderVal > totalFinalVal) {
                setError("preOrder", "invalid-preOder", "số tiền trả trước bạn nhập lớn hơn tổng cộng");
                check = 1;
                return check;
            } else {
                setSuccess("preOrder", "invalid-preOder");
                check = 0;
                return check;
            }
        } else {
            setError("preOrder", "invalid-preOder", "Vui lòng nhập số");
            check = 1;
            return check;
        }
    }
}

function checkEmptyInput() {
    var check = 0;
    if ($("#dateInput").val() == '') {
        setError("dateInput", "invalid-date", "Vui lòng chọn ngày đặt sân");
        check = 1;
    }
    if ($("#timestart").val() == '') {
        setError("timestart", "invalid-time-start", "Vui lòng chọn thời gian bắt đầu");
        check = 1;
    }
    if ($("#timeend").val() == '') {
        setError("timeend", "invalid-time-end", "Vui lòng chọn thời gian kết thúc");
        check = 1;
    }
    if ($('#preOrder').val() == '') {
        setError("preOrder", "invalid-preOder", "vui lòng nhập số tiền trả trước");
        check = 1;
    }
    return check;
}

function checkTime() {
    let dateBooking = $("#dateInput").val();
    if (dateBooking == '') {
        setError("dateInput", "invalid-date", "Vui lòng chọn ngày đặt sân");
    } else {
        setSuccess("dateInput", "invalid-date");
    }
}

function checkDateBooking() {
    checkTime();
    let subPitchId = $("#subPitchId").text();
    let dateBooking = $("#dateInput").val();
    let timeStart = $("#timestart").val();
    let timeEnd = $("#timeend").val();
    $.ajax({
        type: "GET",
        url: "/checkDateBooking",
        data: {dateBooking, subPitchId, timeStart, timeEnd},
        success: function (result) {
            $('#listTimeBooked').empty();
            if (result.status == "success") {
                $.each(result.data,
                    function (i, item) {
                        let info =
                            '<div class="col-sm-4 pb-0  " style="padding: 3px;">\n' +
                            '                            <div class="alert alert-success " style="text-align: center;"> ' + item.timeStart + ' - ' + item.timeEnd + '\n' +
                            '                            </div>\n' +
                            '                        </div>';
                        if (item.timeStart != null) {
                            $('#listTimeBooked').append(info);
                        }
                        $('.error-message').text(item.message);
                        setErrorForTimeInput(item.message);
                        if ($('.error-message').text() != "khung giờ bạn chọn hợp lệ") {
                            document.getElementById("hourFee").innerText = 0;
                        }

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