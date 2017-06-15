function changeParagraph() {
    var MyElement = document.getElementById("demo");
    MyElement.style.color = "red";
    //MyElement.getElementById("demo").innerHTML = "Paragraph changed.";
    MyElement.innerHTML = "paragrph changed .";


    var text;
    var userNameField = document.getElementById("user_name").value;
    var returnText = document.getElementById("showResult");
    if (userNameField != '') {
        text = "user exists";
    }
    else {
        text = "user not valid";
    }
    returnText.innerHTML = text;
}

function changeMouseText(obj) {
    obj.style.backgroundcolor = "LightBlue";
    obj.innerHTML = "olle";
}

function mouseOut(obj) {
    obj.innerHTML = "login"
    obj.style.backgroundcolor = "LightPink";
}

$(document).ready(function () {
    $("#JQueryButton").click(function () {
        $("#showResult").text("jquery button has been clicked");
    });
    $("input").focus(function () {
        $(this).css("background-color", "LightGray");
    });
    $("input").blur(function () {
        $(this).css("background-color", "white");
    });
    $('#contact').click(function () {
        $("#homePageImages").hide();
        $("#contactDiv").css("visibility", "visible");
    });
    $('#ISR-lan').click(function () {
        window.location = 'HomeP.html';
    });
    $('#USA-lan').click(function () {
        window.location = 'HomePEN.html';
    });

    function displayNextImage() {
        $("#image").fadeOut("slow", function () {
            $(this).attr("src", homePageImagesArray[i]);
            $(this).fadeIn("slow", function () {
                if (i == homePageImagesArray.length - 1) {
                    i = 0;
                }
                else {
                    i++;
                }
            });
        });
    }

    setInterval(displayNextImage, 6000);
    var homePageImagesArray = []; //new Array("images/image1.jpg", "images/image2.jpg", "images/image3.jpg");
    var i = 1;
    homePageImagesArray[1] = "images/law5.jpg";
    homePageImagesArray[2] = "images/law6.jpg";
    homePageImagesArray[0] = "images/bard.png";
});
