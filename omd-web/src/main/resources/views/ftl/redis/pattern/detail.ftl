<!DOCTYPE html>
<html>
<head>
<title>Redis/Pattern/Detail</title>
</head>
<body>
	<form class="form-horizontal" role="form" method="POST"
		action="<#if pattern??>update<#else>create</#if>.do"
		style="margin-top: 40px; margin-left: 30%; width: 40em">
		<#if pattern??>
			<input style="display:none" type="text" name="id" value="${pattern.id}">
		</#if>
		<div class="form-group">
			<label for="pattern" class="col-sm-2 control-label">Pattern：</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" name="pattern" placeholder="pattern string"
				value="<#if pattern??>${pattern.pattern}</#if>">
			</div>
		</div>
		<div class="form-group">
			<label for="db" class="col-sm-2 control-label">Redis Db：</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" name="db" placeholder="redis db number"
				value="<#if pattern??>${pattern.db}</#if>">
			</div>
		</div>
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">简称：</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" name="name" placeholder="简称"
				value="<#if pattern??>${pattern.name}</#if>">
			</div>
		</div>
		<div class="form-group">
			<label for="desp" class="col-sm-2 control-label">描述：</label>
			<div class="col-sm-10">
				<textarea type="text" class="form-control" name="desp" placeholder="描述"><#if pattern??>${pattern.desp}</#if></textarea>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button " type="submit" class="btn btn-default" ><#if pattern??>更改<#else>创建</#if></button>
			</div>
		</div>
	</form>
</body>
</html>
