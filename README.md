# usedbook2

### 2022-01-09

로그인, 회원가입 test코드를 작성하면서 

MockMvc로 서버를 띄우지않고서도 테스트가 가능하다는 것을 알게되었다


### 2022-01-10

코드를 보면 Check메서드로 실시간으로 validation을 받아와서 화면에 에러메시지를 보여주고있다.

실시간으로 유효성 검증을 하면서 문제가 없을때만 submit버튼이 활성화되게끔 처리해놓아서 controller에는 검증된 값만 들어오긴할텐데 

실제 DB와 연결하여 저장하는 Save메서드에서도 검증을 안하고 값을 저장하기에는 불안하고.. 

그래서 save에서 또다시 검증 메서드를 불러와서 확인하고있는데 이렇게 2가지 메서드로 나누어서 처리하는게 맞는건가 싶다

그렇다고 굳이 js에서 또다시 검증로직을 작성하면 나중에 서버에서 검증로직 추가하면 js에서도 맞추어서 검증로직을 써주어야하는거니까.. 

어떻게 하면 더 발전할 수 있는지 고민해야겠다.



    @PostMapping("/register")
    public String registerSave(@Validated @ModelAttribute MemberDTO.RegisterForm registerForm, BindingResult bindingResult){
        List list = registerCheck(registerForm, bindingResult);
        if(list != null){
            return "member/register";
        }

        memberService.registerSave(registerForm);
        return "redirect:/registerOk";
    }

    @PostMapping("/registerCheck")
    @ResponseBody
    public List registerCheck(@Validated @ModelAttribute MemberDTO.RegisterForm registerForm, BindingResult bindingResult){
        Member byEmail = memberRepository.findByEmail(registerForm.getEmail());
        if(byEmail != null){
            bindingResult.rejectValue("email", "duplicate", "이미 존재하는 이메일입니다.");
        }

        Member byNickName = memberRepository.findByNickName(registerForm.getNickname());
        if(byNickName != null){
            bindingResult.rejectValue("nickname", "duplicate", "이미 존재하는 닉네임입니다.");
        }

        if(bindingResult.hasErrors()){
            return new ValidResultList(bindingResult).getList();
        }
        return null;
    }
    //------------------------------------------------------------------------------------
    function registerCheck(element){
        var current = $(element).attr("id");
        var email = $.trim($("#email").val());
        var nickname = $.trim($("#nickname").val());
        var password = $.trim($("#password").val());

        $.ajax({
            type:"post",
            url:"/registerCheck",
            contentType: 'application/x-www-form-urlencoded',
            data: {
                "email" : email,
                "nickname" : nickname,
                "password" : password
            },
            success: function(lists){
                validProcess(lists, current);
            },
            error:function(error){
            }
        });
    }

    function validProcess(lists, current){
        var currentValid;
        if(lists.length > 0){
            validENP = false;
             currentValid = lists.find(function(element){
                if(element.field === current){
                    return true;
                }
            });     //currentValid = {field: 'email', message: '중복되는 이메일이 있습니다'}

            if(currentValid != undefined){
                inputClass(current, "red");
                feedbackClass(current, "block", currentValid.message);
            }else{
                inputClass(current, "green");
                feedbackClass(current, "none", null);
            }
        }else{
            validENP = true;
            inputClass(current, "green");
            feedbackClass(current, "none", null);
        }
    }


비밀번호 찾기 이메일 전송 기능을 구현하면서 개인정보가 들어있는 yml파일을 함께 push해버렸다;;

revert하다가 무언가 꼬여서 git reset 기능을 알게되어서 아예 다 롤백해버렸는데.. 

협업 중인 상황이라면 revert도 무서울텐데 ㅜㅜ 파일 잘못올렸을때 취소하는 방법에 대해 더 알아봐야겠다.


### [2022-01-13]

api와 rest api를 찾아보면서 내가 이해한대로 적어보겠다

REST API : 

어떻게 동작하는지는 모르지만 api문서에 기술된대로 "외부에" 요청을 보내면 "외부에서 처리해서 결과값을 반환" 해주는 것

(ex. youtube api 요청 : https://developers.google.com/youtube/v3/docs/search/list   GET https://www.googleapis.com/youtube/v3/search  )



내가 구현하는 서비스가 "나의 웹 프로젝트" 에서만 구동될 것이라면 굳이 restful하게 구현하지 않아도된다

왜냐? rest api라는 자체가 view를 전달하지 않고 결과를 json형식으로 보내주는 것으로

"다른 웹 프로젝트" 또는 "다른 어플 프로젝트" 등의 >>>>다양한 곳에서 웹 서비스를 편리하게 사용하기 위해<<<< 나온 개념이기 때문 (그래서 rest api는 요청의 통일성을 위해 url 패턴과 http method를 약속했다)

하지만 단독 웹 프로젝트를 진행할때 api로 구현하는 경우도 있다고한다

이유는 향후 다른 프로그램에서의 확장성때문 (클라이언트가 더이상 브라우저에만 있지 않으니까..)





