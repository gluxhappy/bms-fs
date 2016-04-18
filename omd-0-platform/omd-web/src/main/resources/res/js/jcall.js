function jcall(url,params,success,error){
    $.ajax({
        url:url,
        type:"POST",
        contentType:"application/json",
        dataType:"json",
        data:JSON.stringify(params),
        success:function(data,status){
            if(status=='success' && data != null ){
                if(data.status=="SUCCESS"){
                    success(data.data);
                }else{
                    error(data.code,data.message);
                }
            }else{
                error(0,"ajax error.");
            }
        },
        error:function(xhr){
            error(0,"server ajax error");
        }
    });
}