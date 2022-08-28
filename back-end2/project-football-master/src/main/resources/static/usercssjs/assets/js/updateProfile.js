//
// /*---------------------------- --------------------*/
//
//
//
//
//
//
//
// /*----------------------- check phone----------------------------*/
//
//             var clickHidePhone = document.getElementById('idPhone');
//                 clickHidePhone.addEventListener('click',function(){
//                 document.getElementById('phone_errorNull').classList.add('hidden');
//             })
//
//          function validatePhoneNumber(input_str) {
//             var re = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im;
//             return re.test(input_str);
//         }
//
//         function checkPhone() {
//             var phone = document.getElementById('idPhone').value;
//             if(phone==""){
//                 document.getElementById('phone_errorNull').classList.remove('hidden');
//             }else{
//                 if (!validatePhoneNumber(phone)) {
//                 document.getElementById('phone_error').classList.remove('hidden');
//
//                  } else {
//                     document.getElementById('phone_error').classList.add('hidden');
//                  }
//             event.preventDefault();
//             }
//         }
//        document.getElementById('myformPhone').addEventListener('submit', checkPhone);
//
// /*-------------------------check email----------------------------*/
//
//             var clickHideEmail = document.getElementById('idEmail');
//                 clickHideEmail.addEventListener('click',function(){
//                 document.getElementById('email_errorNull').classList.add('hidden');
//             })
//           function validateEmail(email)
//             {
//               var re = /^(?:[a-z0-9!#$%&amp;'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&amp;'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/;
//
//               return re.test(email);
//             }
//
//              function checkEmail() {
//             var email = document.getElementById('idEmail').value;
//             if(email ==""){
//                 document.getElementById('email_errorNull').classList.remove('hidden');
//             }else{
//                 if (!validateEmail(email)) {
//                     document.getElementById('email_error').classList.remove('hidden');
//                 }else {
//                     document.getElementById('email_error').classList.add('hidden');
//                 }
//             event.preventDefault();
//             }
//         }
//        document.getElementById('myformEmail').addEventListener('submit', checkEmail);
//
//
//     /*------------------------check Empty ---------------------*/
//        function checkEmpty(){
//             var name = document.getElementById('idName').value;
//             var date = document.getElementById('idDate').value;
//             var address = document.getElementById('idAddress').value;
//
//             if(name==""){
//                 document.getElementById('name_errorNull').classList.remove('hidden');
//             }if(date ==""){
//                 document.getElementById('date_errorNull').classList.remove('hidden');
//             }if(address==""){
//                 document.getElementById('address_errorNull').classList.remove('hidden');
//             }
//         }
//         /*--------------------- check Password -----------------------*/
//         function checkPassword(){
//         var Regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
//         var newPass = document.getElementById('idNewPassword').value;
//         var conPass = document.getElementById('idConfirmPassword').value;
//         if(conPass==""){
//         document.getElementById('confirmPassword_errorNull').classList.remove('hidden');
//         }
//         if(newPass==""){
//             document.getElementById('newPassword_errorNull').classList.remove('hidden');
//         }else{
//             if(newPass!=Regex){
//                  document.getElementById('newPass_error').classList.remove('hidden');
//                  newPass == new newPass;
//                 }else{
//                     if(newPass != conPass){
//                         document.getElementById('ConPass_error').classList.remove('hidden');
//                         conPass = new conPass;
//                     }
//                 }
//             }
//         }
//   /*-------------------------- hide/show newPassword------------------------*/
//          var inputNewPass = document.getElementById('idNewPassword');
//          var btnNewPass = document.getElementById('btnPassword');
//            btnNewPass.addEventListener('click', function(){
//            var setTypeNewPass = inputNewPass.getAttribute('type');
//              inputNewPass.setAttribute('type',setTypeNewPass === 'text' ? 'password' : 'text')
//            })
//
// /*-------------------------- hide/show Confirm Password------------------------*/
//          var inputConPass = document.getElementById('idConfirmPassword');
//          var btnConPass = document.getElementById('btnConPassword');
//            btnConPass.addEventListener('click', function(){
//            var setTypeConPass = inputConPass.getAttribute('type');
//             inputConPass.setAttribute('type',setTypeConPass === 'text' ? 'password' : 'text')
//         })
//
//  /*------------------------- Hide Password -------------------------------------*/
//
//         var changePass = document.getElementById('idChangePassword');
//         changePass.addEventListener('click',function(){
//             document.getElementById('idHidePassword').classList.remove('hidden');
//         })