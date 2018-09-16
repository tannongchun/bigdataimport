/**
 * 初始化页面
 */

;
var BASE_URL="/"

// 主函数
$(function () {
    // 确定导入文件
    $("#imporfile").bind("click",function () {

        var fileObj = document.getElementById("fileupload").files[0]; // js 获取文件对象
        if (typeof (fileObj) == "undefined" || fileObj.size <= 0) {
           alert("请选择文件")
            return;
        }
        var formFile = new FormData();
        formFile.append("file", fileObj); //加入文件对象
        var data = formFile;
        $.ajax({
            // 上传文件的地址
            url: BASE_URL.concat("api/v1/uploads/excel/"),
            data: data,
            type: "post",
            dataType: "json",
            cache: false,//上传文件无需缓存
            processData: false,//用于对data参数进行序列化处理 这里必须false
            contentType: false, //必须
            success: function (result) {
                if(result.code == 0){
                    alert(result.msg);
                }else {

                }
            },
        })
    });
})

