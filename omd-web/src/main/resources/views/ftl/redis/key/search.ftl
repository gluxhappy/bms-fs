<!DOCTYPE html>
<html>
<head>
	<title>Profile/Info</title>
</head>
<body>
	<div <#if !keys?? > style="margin-top:15%"</#if>>
	   <form role="form" action="search" style="margin-left:30%;width:400px">
	      <div class="input-group">
	         <input type="text" class="form-control" placeholder="Key模式" name="pattern" value="<#if pattern??>${pattern}<#else>*</#if>" />
	         <span class="input-group-addon">@</span>
	         <select class="form-control" name="db">
	         	<option value="3">3</option>
	         	<option value="4">4</option>
	         </select>
	      </div>
	      <input  style="margin-top:10px;margin-left:40%;width:100px" type="submit" value="Search" class="btn btn-primary"/>
	   </form>
	</div>

	<#if keys??>
	    <#assign maxsize = 2000>
		<table class="table table-hover">
		   <caption>共有结果${keys?size}条，最多显示${maxsize}条</caption>
		   <thead>
		      <tr>
		         <th>Id</th>
		         <th>Key</th>
		         <th>操作</th>
		      </tr>
		   </thead>
		   <tbody>
    		    <#assign idx = 0>
				<#list keys as key> 
				    <#assign idx=idx+1>
    				<#if idx gt maxsize>
    				      <#break>
    				<#else>
                        <tr id="key_${idx}">
	                        <td style="width:5em;">${idx}</td>
	                        <td class="key" data-db="${key.db}">${key.key?html}</td>
	                        <td>
	                        	<div class="btn-group" style="margin-left:10px">
	                        		  <a href="#" class="btn btn-default detail">详情</a>
	                        		  <a href="#" class="btn btn-default delete" >删除</a>
								</div>
	                       	</td>
                        </tr>
    				</#if>
				</#list> 
		   </tbody>
		</table>
		<#if keys?size gt maxsize>
		  <p class="text-center text-danger">还有${keys?size - maxsize}条结果未显示。</p>
		</#if>
		<div id="keyinfo" class="modal hide fade in" style="display: none; ">
			<div class="modal-header">
				<a class="close" data-dismiss="modal">×</a>
				<h3>This is a Modal Heading</h3>
			</div>
			<div class="modal-body">
				<h4>Text in a modal</h4>
				<p>You can add some text here.</p>		        
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-success">Call to action</a>
				<a href="#" class="btn" data-dismiss="modal">Close</a>
			</div>
		</div>
		<div style="width:100%;height:40px;" ></div>
		<script>
			function findKeyElement(current){
				var parent=$(current);
				do{
					parent=parent.parent();
				}while(!parent.is("tr"));
				var tr=parent;
				key=$(tr.children(".key")[0]);
				
		        var result={
		                key:key.text(),
		                db:key.attr("data-db"),
		                tr:tr
		            };
		        return result;
			}
			function loadKeyDesp(key,db,target){
		        var params={
		                key:key,
		                db:db
		            };	        
		        jcall(
		        		"detail.do",
		        		params,
		        		function(data){
		        			target=$(target);
		        			var timeStr;
		        			if(data.ttl==-1){
		        				timeStr="永不过期";
		        			}else{
		        				var days=0;
		        				var hours=0;
		        				var minutes=0;
		        				var seconds=data.ttl;
		        				if(seconds>=60){
		        					minutes=Math.floor(seconds/60);
		        					seconds= data.ttl%60;
		        					if(minutes>=60){
		        						hours=Math.floor(minutes/60);
		        						minutes=minutes%60
		        						if(hours>=24){
		        							days=Math.floor(hours/24);
		        							hours=hours%24;
		        						}
		        					}
		        				}
		        				timeStr=(days>0?(days+"天"):"")+
		        						(hours>0?(hours+"时"):"")+
		        						(minutes>0?(minutes+"分"):"")+
		        						(seconds>0?(seconds+"秒"):"")+"("+data.ttl+")";
		        			}
		        			target.attr(
		        			"data-content",
		        			"<p class='text-left'>类型:"+data.type+"</p>"+
		        			"<p class='text-left'>过期时间:"+timeStr+"</p>"+
		        			"<p class='text-left'>数据库:"+data.db+"</p>");
							target.attr("init","true");
							target.popover("show");
		        		},
		        		function(code,msg){
		        			alert("Error["+code+"]:"+msg);
		        		});
			}
			$("a.detail").click(
				function(){
					if($(this).attr("init")!="true"){
						var result=findKeyElement(this);
						$(this).popover({
							title:result.key,
							html:true,
							placement:"left",
							trigger:"hover"
						});
						loadKeyDesp(result.key,result.db,this);
					}
					return false;
				}
			);
			$("a.delete").click(function(){
				var result=findKeyElement(this);
		        var params={
		                key:result.key,
		                db:result.db
		            };	        
		        jcall(
		        		"delete.do",
		        		params,function(data){
		        			if(data == "SUCCESS"){
		        				tr.remove();
		        			}
		        		},
		        		function(code,msg){
		        			alert("Error["+code+"]:"+msg);
		        		});
				return false;
			});
		</script>
	</#if>
</body>
</html>
