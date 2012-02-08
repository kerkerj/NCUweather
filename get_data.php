<?php
	header("Content-Type:text/html; charset=utf-8"); 
	
	//抓取網頁
	$url = "http://pblap.atm.ncu.edu.tw/ncucwb/indexReal.asp";
	$contents = file_get_contents($url); 	
	$contents = iconv("big5", "utf-8", $contents); 	//轉碼
	
	//分析資料
	$data_array = explode("&nbsp;", $contents);
	foreach($data_array as $index => $value)
		$data[$index] = $value;
		
	$tempa = explode(" ", $data[1] );	//日期
	$tempb = explode(" ", $data[2] );	//天氣
	$temp0 = explode(" ", $data[5] );	//溫度數據
	$temp1 = explode(" ", $data[7] );	//濕度數據
	$temp2 = explode(" ", $data[9] ); 	//風向數據
	$temp3 = explode(" ", $data[11] );	//風速數據
	$temp4 = explode(" ", $data[13] );	//氣壓數據
	$temp5 = explode(" " , $data[15]);	//降雨數據
	$temp6 = explode("<br>" , $data[15]);//天氣型態
	$temp7 = explode("</font>", $temp6[2]);//天氣型態數據
	
	//處理手機端要求
	$agent = $_POST['user_agent'];
	if ( $agent === "Android")
	{
		//轉成json
		$json_data = array(
			'date' => $tempa[1],
			'time' => $tempb[2],
			'temperature' => $temp0[1],
			'humidity' => $temp1[1],
			'wind_direction' => $temp2[1],
			'wind_velocity' => $temp3[1],
			'pressure' => $temp4[1],
			'rain' => $temp5[1],
			'weather' => $temp7[0],
			'error_code' => "0",
			'error_msg' => "0"
		);
		$android = json_encode($json_data);
		echo $android;
	}
	else	//網頁端XD
	{
		echo "日期：$data[1]"."<br>";
		echo "時間：$data[2]"."<br>";
		echo "溫度： "; 
		echo "$temp0[1] ℃"."<br>";
		echo "濕度： "; 
		echo "$temp1[1] %"."<br>";
		echo "風向： "; 
		echo "$temp2[1] °"."<br>";
		echo "風速： "; 
		echo "$temp3[1] m/s"."<br>";
		echo "氣壓： "; 
		echo "$temp4[1] hPa"."<br>";
		echo "降雨： "; 
		echo "$temp5[1] mm/hr"."<br>";	
		echo "天氣： $temp7[0]";
	}
	