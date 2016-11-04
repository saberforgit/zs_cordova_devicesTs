**水印模块使用方法**
@方法：createWaterMark ：添加水印
@方法参数：options = {
    type(添加水印的类型):string(),
    postion:"middle",
    stringMarkText:"FFSSF",
    sourcePhotoPath:"",
    markPhotoPath:"file:///android_asset/www/img/logo.png",
    sourcePhotoBase64:base64,
    penColor:"#000FFF",
    inSampleSize:6,
    stringMarkSize:14,
    alpha:80,
    leftStringPadding:15,
    rightStringPadding:15,
    bottomStringPadding:15,
    topStringPadding:15,
    leftPhotoPadding;
    rightPhotoPadding;
    bottomPhotoPadding;
    topPhotoPadding;
    }


   1》参数：var param = {"width":1300,"height":700,"minWidth":1,"maxWidth":7,"penColor":"#000FFF"};
   width：模块宽度  height：模块高度  minWidth：最小笔画宽度  maxWidth:最大笔画宽度  penColor：笔画颜色
   2》调用： cordova.plugins.signaturePad.showSignature(param,function (res) {
                                                        alert(JSON.stringify(res));
                                                        document.getElementById("image").src=res.imageUrl;
                                                        }, function (error) {
                                                        })

注：添加完插件后需要在代码中修改包名
com.zsmarter.cordova.signaturepad.SignatureActivity 中的import io.cordova.hellocordova.R  改为自己的包名，
例如 com.zsmarter.pandanfance.R。
com.zsmarter.cordova.signaturepad.viewsSignaturePad 中的修改同上。
