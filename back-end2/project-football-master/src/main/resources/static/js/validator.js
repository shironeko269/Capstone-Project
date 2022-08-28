// Đối tượng `Validator`
function Validator(options) {
    function getParent(element, selector) {
        while (element.parentElement) {
            if (element.parentElement.matches(selector)) {
                return element.parentElement;
            }
            element = element.parentElement;
        }
    }

    var selectorRules = {};

    // Hàm thực hiện validate
    function validate(inputElement, rule) {
        var errorElement = getParent(inputElement, options.formGroupSelector).querySelector(options.errorSelector);
        var errorMessage;

        // Lấy ra các rules của selector
        var rules = selectorRules[rule.selector];

        // Lặp qua từng rule & kiểm tra
        // Nếu có lỗi thì dừng việc kiểm
        for (var i = 0; i < rules.length; ++i) {
            switch (inputElement.type) {
                case 'radio':
                case 'checkbox':
                    errorMessage = rules[i](
                        formElement.querySelector(rule.selector + ':checked')
                    );
                    break;
                default:
                    errorMessage = rules[i](inputElement.value);
            }
            if (errorMessage) break;
        }

        if (errorMessage) {
            errorElement.innerText = errorMessage;
            getParent(inputElement, options.formGroupSelector).classList.add('invalid');
        } else {
            errorElement.innerText = '';
            getParent(inputElement, options.formGroupSelector).classList.remove('invalid');
        }

        return !errorMessage;
    }

    // Lấy element của form cần validate
    var formElement = document.querySelector(options.form);
    if (formElement) {
        // Khi submit form
        formElement.onsubmit = function (e) {
            e.preventDefault();

            var isFormValid = true;

            // Lặp qua từng rules và validate
            options.rules.forEach(function (rule) {
                var inputElement = formElement.querySelector(rule.selector);
                var isValid = validate(inputElement, rule);
                if (!isValid) {
                    isFormValid = false;
                }
            });

            if (isFormValid) {
                // Trường hợp submit với javascript
                if (typeof options.onSubmit === 'function') {
                    var enableInputs = formElement.querySelectorAll('[name]');
                    var formValues = Array.from(enableInputs).reduce(function (values, input) {

                        switch(input.type) {
                            case 'radio':
                                values[input.name] = formElement.querySelector('input[name="' + input.name + '"]:checked').value;
                                break;
                            case 'checkbox':
                                if (!input.matches(':checked')) {
                                    values[input.name] = '';
                                    return values;
                                }
                                if (!Array.isArray(values[input.name])) {
                                    values[input.name] = [];
                                }
                                values[input.name].push(input.value);
                                break;
                            case 'file':
                                values[input.name] = input.files;
                                break;
                            default:
                                values[input.name] = input.value;
                        }

                        return values;
                    }, {});
                    options.onSubmit(formValues);
                }
                // Trường hợp submit với hành vi mặc định
                else {
                    formElement.submit();
                }
            }
        }

        // Lặp qua mỗi rule và xử lý (lắng nghe sự kiện blur, input, ...)
        options.rules.forEach(function (rule) {

            // Lưu lại các rules cho mỗi input
            if (Array.isArray(selectorRules[rule.selector])) {
                selectorRules[rule.selector].push(rule.test);
            } else {
                selectorRules[rule.selector] = [rule.test];
            }

            var inputElements = formElement.querySelectorAll(rule.selector);

            Array.from(inputElements).forEach(function (inputElement) {
                // Xử lý trường hợp blur khỏi input
                inputElement.onblur = function () {
                    validate(inputElement, rule);
                }

                // Xử lý mỗi khi người dùng nhập vào input
                inputElement.oninput = function () {
                    var errorElement = getParent(inputElement, options.formGroupSelector).querySelector(options.errorSelector);
                    errorElement.innerText = '';
                    getParent(inputElement, options.formGroupSelector).classList.remove('invalid');
                }
            });
        });
    }

}



// Định nghĩa rules
// Nguyên tắc của các rules:
// 1. Khi có lỗi => Trả ra message lỗi
// 2. Khi hợp lệ => Không trả ra cái gì cả (undefined)
Validator.isRequired = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined :  message || 'Vui lòng nhập trường này'
        }
    };
}

Validator.isEmail = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            return regex.test(value) ? undefined :  message || 'Trường này phải là email';
        }
    };
}

Validator.minLength = function (selector, min, message) {
    return {
        selector: selector,
        test: function (value) {
            return value.length >= min ? undefined :  message || `Vui lòng nhập tối thiểu ${min} kí tự`;
        }
    };
}
Validator.maxLength = function (selector, max, message) {
    return {
        selector: selector,
        test: function (value) {
            return value.length <= max ? undefined :  message || `Vui lòng nhập tối đa ${max} kí tự`;
        }
    };
}
Validator.isBlank = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            if (value.indexOf(' ') >= 0) {
                return value ? message || 'Vui lòng nhập trường này' : undefined
            }
        }
    };
}
Validator.isNotCharacterSpecial = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^[a-zA-Z0-9&._-]+$/;
            return regex.test(value) ? undefined :  message || 'Không được nhập kí tự đặt biệt';
        }
    };
}
Validator.isCharacterSpecial = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^[-@.\/#&+\w\s]*$/;
            return regex.test(value) ? undefined :  message ;
        }
    };
}
Validator.isCutManyBlankByFullName = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^[\p{L}'\-\.]+( [\p{L}'\-\.]+)*$/u;
            return regex.test(value) ? undefined :  message ||
                'Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách!';
        }
    };
}
Validator.isCutManyBlankByAddress = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^(?=.*[a-z|A-Z].{2,})[\p{L}'\-\.0-9-@!#$%^&*?<>:,;'/\-_.\/#&+\w\s]+( [\p{L}'\-\.0-9]+)*$/u;
            return regex.test(value) ? undefined :  message ||
                'Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!';
        }
    };
}
Validator.isCutManyBlankByProduct = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^(?=.*[a-z|A-Z])[\p{L}'\-\.0-9-@!#$%^&*?<>:,;'/\-_.\/#&+\w\s]+( [\p{L}'\-\.0-9]+)*$/u;
            return regex.test(value) ? undefined :  message ||
                'Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách!';
        }
    };
}
Validator.isCutManyBlankByDescription = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^(?=.*[a-z|A-Z].{5,})[\p{L}'\-\.0-9-@!#$%^()*~`&*?<>:,;'/\-_.\/#&+\w\s]+( [\p{L}'\-\.0-9]+)*$/u;
            return regex.test(value) ? undefined :  message ||
                'Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 5 chữ cái!';
        }
    };
}
Validator.isPassword = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/;
            return regex.test(value) ? undefined :  message ||
                'Phải có ít nhất 1 chữ hoa,1 chữ thường,1 số, và tối thiểu 8 kí tự';
        }
    };
}
Validator.isPhone = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;
            return regex.test(value) ? undefined :  message || 'Phải nhập đúng số điện thoại!';
        }
    };
}
Validator.isNumber = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^[1-9]['\-\.0-9,.]*$/;
            return regex.test(value) ? undefined :  message || 'Phải nhập số!';
        }
    };
}
Validator.isNumberHave0 = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^[0-9]['\-\.0-9,.]*$/;
            return regex.test(value) ? undefined :  message || 'Phải nhập số!';
        }
    };
}
// Validator.isDiscount = function (selector, message) {
//     return {
//         selector: selector,
//         test: function (value) {
//             var regex = /^(?:[0-9]|[1-9][0-9](\.\d{1,2})?|100|10.00||1.0||0)$/;
//             return regex.test(value) ? undefined :  message || 'Phải nhập số!';
//         }
//     };
// }
Validator.isDiscountDecimal = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^[0-9]{1,11}(?:\.[0-9]{1,3})?$/;
            return regex.test(value) ? undefined :  message || 'Số thập phân chỉ gồm 2!';
        }
    };
}
Validator.isConfirmed = function (selector, getConfirmValue, message) {
    return {
        selector: selector,
        test: function (value) {
            return value === getConfirmValue() ? undefined : message || 'Giá trị nhập vào không chính xác';
        }
    }
}
//hieu
Validator.isNumberGreaterThan0 = function (selector) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^(0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*)$/;
            return regex.test(value) ? undefined : 'Số nhập vào phải lớn hơn 0!';
        }
    };
}
Validator.isCutManyBlankByService = function (selector) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^(?=.*[a-z|A-Z])[\p{L}'\-\.0-9]+( [\p{L}'\-\.0-9]+)*$/u;
            return regex.test(value) ? undefined :
                'Chỉ 1 khoảng trắng giữa 2 kí tự, cuối và đầu chuỗi không có dấu khoảng cách!';
        }
    };
}
