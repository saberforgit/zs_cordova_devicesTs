**ˮӡģ��ʹ�÷���**
@������createWaterMark �����ˮӡ
@����������options = {
    type(���ˮӡ������):string(),
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


   1��������var param = {"width":1300,"height":700,"minWidth":1,"maxWidth":7,"penColor":"#000FFF"};
   width��ģ����  height��ģ��߶�  minWidth����С�ʻ����  maxWidth:���ʻ����  penColor���ʻ���ɫ
   2�����ã� cordova.plugins.signaturePad.showSignature(param,function (res) {
                                                        alert(JSON.stringify(res));
                                                        document.getElementById("image").src=res.imageUrl;
                                                        }, function (error) {
                                                        })

ע�������������Ҫ�ڴ������޸İ���
com.zsmarter.cordova.signaturepad.SignatureActivity �е�import io.cordova.hellocordova.R  ��Ϊ�Լ��İ�����
���� com.zsmarter.pandanfance.R��
com.zsmarter.cordova.signaturepad.viewsSignaturePad �е��޸�ͬ�ϡ�
