# 원주시 ITS - API

## 버스 정류장 정보 
- http://its.wonju.go.kr/map/AjaxRouteListByStop.do
- HTTP/1.1 POST
- stop_id={정류장 번호}




## 정류장 지도 정보
- http://its.wonju.go.kr/map/BusMap.do
- HTTP/1.1 POST
- mapType=STOP&searchType=S&searchKeyword={정류장 검색 키워드}
- Raw output :
`[{"STYPENAME":"일반형 시내/농어촌 버스","STOP_ANGLE":8,"LINK_VTX_ORD":2,"STOP_EXPLAIN":"원주","REGIST_DT":null,"LINK_STOP_LEN":25,"LNG":127.8295668,"REMARK":"원주시 무실동","DEPARTURE_DETECT_RADIUS":30,"USE_FLAG":"1","ARRIVAL_DETECT_RADIUS":30,"SERVICE_ID":67018,"LINK_STOP_ORD":1,"LAT":37.326621,"STOP_TYPE":"1","LINK_LAT":37.3266347,"LINK_ID":2510130900,"STOP_ID":251067018,"STOP_SHORTNAME":"부영아파트","UPDATE_DT":null,"CENTERLANE_FLAG":"0","ADM_DISTRICT_ID":3202067,"FLAG":"1","LINK_LNG":127.8294992,"STOP_NAME":"부영아파트"},{"STYPENAME":"일반형 시내/농어촌 버스","STOP_ANGLE":41,"LINK_VTX_ORD":1,"STOP_EXPLAIN":"원주시청","REGIST_DT":null,"LINK_STOP_LEN":77,"LNG":127.9265885,"REMARK":"원주시 문막읍","DEPARTURE_DETECT_RADIUS":30,"USE_FLAG":"1","ARRIVAL_DETECT_RADIUS":30,"SERVICE_ID":11045,"LINK_STOP_ORD":1,"LAT":37.332399,"STOP_TYPE":"1","LINK_LAT":37.3320534,"LINK_ID":2510593490,"STOP_ID":251011045,"STOP_SHORTNAME":"부영아파트","UPDATE_DT":null,"CENTERLANE_FLAG":"0","ADM_DISTRICT_ID":3202011,"FLAG":"1","LINK_LNG":127.9261915,"STOP_NAME":"부영아파트"},{"STYPENAME":"일반형 시내/농어촌 버스","STOP_ANGLE":256,"LINK_VTX_ORD":3,"STOP_EXPLAIN":"문막","REGIST_DT":null,"LINK_STOP_LEN":65,"LNG":127.829249,"REMARK":"원주시 문막읍","DEPARTURE_DETECT_RADIUS":30,"USE_FLAG":"1","ARRIVAL_DETECT_RADIUS":30,"SERVICE_ID":11044,"LINK_STOP_ORD":1,"LAT":37.32583138,"STOP_TYPE":"1","LINK_LAT":37.32582411,"LINK_ID":2510126600,"STOP_ID":251011044,"STOP_SHORTNAME":"부영아파트","UPDATE_DT":null,"CENTERLANE_FLAG":"0","ADM_DISTRICT_ID":3202011,"FLAG":"1","LINK_LNG":127.8291878,"STOP_NAME":"부영아파트"},{"STYPENAME":"일반형 시내/농어촌 버스","STOP_ANGLE":120,"LINK_VTX_ORD":2,"STOP_EXPLAIN":"대원신협","REGIST_DT":null,"LINK_STOP_LEN":145,"LNG":127.9268157,"REMARK":"원주시 무실동","DEPARTURE_DETECT_RADIUS":30,"USE_FLAG":"1","ARRIVAL_DETECT_RADIUS":30,"SERVICE_ID":67019,"LINK_STOP_ORD":1,"LAT":37.33073473,"STOP_TYPE":"1","LINK_LAT":37.3306866,"LINK_ID":2510159691,"STOP_ID":251067019,"STOP_SHORTNAME":"부영아파트","UPDATE_DT":null,"CENTERLANE_FLAG":"0","ADM_DISTRICT_ID":3202067,"FLAG":"1","LINK_LNG":127.9267741,"STOP_NAME":"부영아파트"}]`

## 버스 정류장 검색

- http://its.wonju.go.kr/map/SearchBusStop.do
- HTTP/1.1 POST
- mapType=STOP&searchType=S&searchKeyword={정류장 검색 키워드}
- Raw output :






					<div class="resultTitle">
	           			
	           			
						<p>
							<img src="/img/bus_route/aside_icon01.gif" alt="아이콘" class="txt_mark">
							<span class="txt_content">
								<span class="txt">부영</span>
								<span class="etc">검색결과</span>
								<span class="cnt">&nbsp;&nbsp;【&nbsp;&nbsp;4&nbsp;&nbsp;건&nbsp;&nbsp;】</span>
							</span>
						</p>
	           			
	           			
	           			
					</div>
					<div class="tab2StoptList">
						
						
						
						<dl class="busStopList">
							<dt>
								<span class="s_nm">부영아파트</span>
								<span class="s_no">(67018)</span>
							</dt>
							<dd class="ddOther">
                           		<input type="hidden" id="stop_id" value='251067018'/>
                           		<input type="hidden" id="lat" value='37.326621'/>
                           		<input type="hidden" id="lng" value='127.8295668'/>
                           		<input type="hidden" id="link_id" value='2510130900'/>
							</dd>
						</dl>							
						
						<dl class="busStopList">
							<dt>
								<span class="s_nm">부영아파트</span>
								<span class="s_no">(11045)</span>
							</dt>
							<dd class="ddOther">
                           		<input type="hidden" id="stop_id" value='251011045'/>
                           		<input type="hidden" id="lat" value='37.332399'/>
                           		<input type="hidden" id="lng" value='127.9265885'/>
                           		<input type="hidden" id="link_id" value='2510593490'/>
							</dd>
						</dl>							
						
						<dl class="busStopList">
							<dt>
								<span class="s_nm">부영아파트</span>
								<span class="s_no">(11044)</span>
							</dt>
							<dd class="ddOther">
                           		<input type="hidden" id="stop_id" value='251011044'/>
                           		<input type="hidden" id="lat" value='37.32583138'/>
                           		<input type="hidden" id="lng" value='127.829249'/>
                           		<input type="hidden" id="link_id" value='2510126600'/>
							</dd>
						</dl>							
						
						<dl class="busStopList">
							<dt>
								<span class="s_nm">부영아파트</span>
								<span class="s_no">(67019)</span>
							</dt>
							<dd class="ddOther">
                           		<input type="hidden" id="stop_id" value='251067019'/>
                           		<input type="hidden" id="lat" value='37.33073473'/>
                           		<input type="hidden" id="lng" value='127.9268157'/>
                           		<input type="hidden" id="link_id" value='2510159691'/>
							</dd>
						</dl>							
						
						
						
						
					</div>
`

## 버스 맵 불러오기

- http://its.wonju.go.kr/map/BusMap.do
- HTTP/1.1 POST
- pageNumber=&board_seq=&mapType=STOP&searchType=S&searchKeyword=%EB%B6%80%EC%98%81&radio_type=S


