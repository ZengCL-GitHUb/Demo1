<template>
    <div id="program-edit"
         v-loading.fullscreen="loading"
         element-loading-text
         element-loading-spinner="iconfont wui-Loading"
         element-loading-background="rgba(0, 0, 0, 0.2)">
        <header-wrap class="operation"
                     :ifFixed="true">
            <!--<div slot="title">PBC&lt;!&ndash;考核设置&ndash;&gt;{{_sn('pm.i18n234')}}</div>-->
            <div slot="title">{{formData.schemename}}</div>
            <div slot="operation">
                <!--步骤条-->
                <Progress :textArr="formData.isresultaudited? stepArr: stepArr2"
                          :active="activeStep"
                          :size="24"
                          :chooseItem="chooseProgress"
                          :itemStyle="(formData.id && !isMiddle)?{cursor:'pointer'}:{}"></Progress>
            </div>
        </header-wrap>
        <div class="basic-back">
            <div></div>
            <el-tabs v-model="activeName"
                     @tab-click="handleClick"
                     class="eltab">
                <el-tab-pane :label="_sn('common.i18n81')"
                             name="0">
                    <!--方案定义-->
                    <middle-view v-if="isMiddle || formData.schememode === 20"
                                 ref="middleView"
                                 :formData="formData"
                                 :addStepFun="addStepFun"
                                 @changeName="changeName"></middle-view>
                    <div class="program-content"
                         v-else>
                        <!--formData.ruletype === 0 ? rules0: rules1formData.ruletype === 0 ? rules0: rules1-->
                        <el-form :model="formData"
                                 ref="formData"
                                 :rules="rules0"
                                 label-width="120px">
                            <el-row>
                                <el-form-item :label="_sn('common.i18n48')"
                                              prop="schemename">
                                    <!--方案名称-->
                                    <el-input v-model="formData.schemename"
                                              placeholder
                                              maxlength="40"></el-input>
                                </el-form-item>
                                <el-form-item :label="_sn('common.i18n30')"
                                              class="zero"
                                              prop="orgid">
                                    <!--所属组织-->
                                    <!-- <el-input v-model="classData.indiclasscode" auto-complete="off"></el-input> -->
                                    <Ref :model="model.org"
                                         class="input_length"
                                         field="refpk"
                                         refCode="org_ref"
                                         :refParam="{refTitle: _sn('common.i18n30')}"
                                         :clientParam="{serviceCode: 'HRPM0010'}"
                                         labelField="refname"
                                         placeholder
                                         @afterReference="afterOrg"
                                         refTo="ref-wrap"
                                         style="width: 100%"></Ref>
                                    <!--,orgtype:1-->
                                </el-form-item>
                                <el-form-item :label="_sn('common.i18n58')"
                                              prop="periodid">
                                    <!--绩效期间-->
                                    <!--<el-input v-model="formData.periodname" placeholder=""></el-input>-->
                                    <Ref :model="model.period"
                                         field="periodid"
                                         refCode="pm_period_grid_ref"
                                         :refParam="{refTitle: _sn('common.i18n58'),
                   customTableHeaders:[
                     {prop: 'refcode',label: _sn('common.i18n11')},
                     {prop: 'refname',label: _sn('common.i18n4')},
                     {prop: 'year',label: _sn('pm.i18n111')}
                   ]}"
                                         labelField="periodname"
                                         @afterReference="afterReference"
                                         placeholder
                                         refTo="ref-wrap"></Ref>
                                    <!--绩效期间-->
                                    <!--header 编码 名称 所属年度-->
                                </el-form-item>
                                <el-form-item :label="_sn('pm.i18n1360')"
                                              prop="applyobj">
                                    <!--考核对象类型-->
                                    <el-select v-model="formData.applyobj"
                                               :disabled="!!formData.id">
                                        <el-option v-for="item in applyobjOption"
                                                   :key="item.id"
                                                   value-key="id"
                                                   :label="item.name"
                                                   :value="item.id"></el-option>
                                    </el-select>
                                    <!-- <el-tooltip content="" class="formtip" placement="top"
                               :visible-arrow="true"  effect="light">
                     <span slot="content"><i class="iconfont wui-Warning"></i> &lt;!&ndash;考核结果等级&ndash;&gt;{{_sn('pm.i18n58')}}，&lt;!&ndash;用于PBC考核之后，得分所对应的等级&ndash;&gt;{{_sn('pm.i18n380')}}</span>
                     <i class="iconfont wui-Warning"></i>
                  </el-tooltip>-->
                                    <!--<el-input v-model="formData.decimaldigits"></el-input>-->
                                </el-form-item>
                                <el-form-item :label="_sn('pm.i18n74')"
                                              prop="scoreruleid">
                                    <!--指标评分规则-->
                                    <Ref :model="model.scorerule"
                                         field="scoreruleid"
                                         refCode="pm_scorerule_grid_ref"
                                         :refParam="{refTitle: _sn('pm.i18n74'),diaWidth: '70%',
                   customTableHeaders:[
                     {prop: 'refname',label: _sn('common.i18n4')},
                     {prop: 'ruletypename',label: _sn('common.i18n100')},
                     {prop: 'remark',label: _sn('common.i18n10')}
                   ]}"
                                         :clientParam="{orgid: formData.orgid}"
                                         labelField="scorerulename"
                                         placeholder
                                         @afterReference="afterScore"
                                         refTo="ref-wrap"></Ref>
                                    <!--指标评分规则-->
                                    <!--header 名称 类型 备注-->
                                    <!-- <el-tooltip content="" class="formtip" placement="top"
                               :visible-arrow="true"  effect="light">
                     <span slot="content"><i class="iconfont wui-Warning"></i>  &lt;!&ndash;设置该考核方案中定性指标的评分规则&ndash;&gt;{{_sn('pm.i18n164')}}</span>
                     <i class="iconfont wui-Warning"></i>
                  </el-tooltip>-->
                                </el-form-item>
                                <el-form-item :label="_sn('pm.i18n58')"
                                              prop="gradeid">
                                    <!--考核结果等级-->
                                    <el-select v-model="formData.gradeid">
                                        <el-option v-for="item in gradeopt"
                                                   :key="item.id"
                                                   value-key="id"
                                                   :label="item.gradename"
                                                   :value="item.id"></el-option>
                                    </el-select>
                                    <!-- <el-tooltip content="" class="formtip" placement="top"
                               :visible-arrow="true"  effect="light">
                     <span slot="content"><i class="iconfont wui-Warning"></i> &lt;!&ndash;考核结果等级&ndash;&gt;{{_sn('pm.i18n58')}}，&lt;!&ndash;用于PBC考核之后，得分所对应的等级&ndash;&gt;{{_sn('pm.i18n380')}}</span>
                     <i class="iconfont wui-Warning"></i>
                  </el-tooltip>-->
                                    <!--<el-input v-model="formData.decimaldigits"></el-input>-->
                                </el-form-item>
                                <!--</el-row>-->
                                <!--<el-row>-->
                                <el-form-item :label="_sn('pm.i18n238')"
                                              prop="scoreway">
                                    <!--总分汇总规则-->
                                    <el-select v-model="formData.scoreway">
                                        <el-option v-for="item in scorewayopt"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="item.value"></el-option>
                                    </el-select>
                                    <!-- <el-tooltip content="" class="formtip" placement="top" effect="light"
                               :visible-arrow="true">
                     <span slot="content"><i class="iconfont wui-Warning"></i> &lt;!&ndash;总分汇总规则说明&ndash;&gt;{{_sn('pm.i18n239')}}...</span>
                     <i class="iconfont wui-Warning"></i>
                  </el-tooltip>-->
                                </el-form-item>
                                <!-- 方案类型 -->
                                <el-form-item v-if="showTargetPannel"
                                              :label="_sn('pm.i18n97')"
                                              required>
                                    <!--方案类型-->
                                    <div class="prog-tag">
                                        <div class="prog-tag-top"
                                             :class="showMoreTags ? 'show-more-prog-tag':''">
                                            <div class="tag-list"
                                                 v-if="tagsList.length > 0">
                                                <el-tag @click="selectTag(index)"
                                                        :class="[item.issystem ? 'system-tag':'', item.isSelect ? 'select-tag':'' ]"
                                                        v-for="(item, index) in tagsList"
                                                        :key="index">{{item.name}}
                                                </el-tag>
                                            </div>
                                            <!-- 暂无标签 -->
                                            <div class="tag-list"
                                                 v-else>({{_sn('pm.i18n1567')}})</div>
                                            <div class="tag-show-more"
                                                 :class=" showMoreTags ? 'pull-icon-trans' : ''"
                                                 @click="showMoreTags = !showMoreTags">
                                                <i class="iconfont wui-Pull-Down_bold"></i>
                                            </div>
                                            <div>
                                                <div class="add-tag"
                                                     v-if="!showAddTag"
                                                     @click="showAddTag = !showAddTag">
                                                    <i class="iconfont wui-Add_bold"></i>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tag-add-input"
                                             v-if="showAddTag">
                                            <!-- 暂无标签名称 -->
                                            <el-input @keyup.enter.native="onTagAdd"
                                                      v-model="addTag"
                                                      :placeholder="_sn('pm.i18n1568')"></el-input>
                                        </div>
                                    </div>
                                </el-form-item>
                                <!-- 指标来源 -->
                                <el-form-item v-if="!isjiazhiguan"
                                              :label="_sn('pm.i18n1570')"
                                              prop="indisource">
                                    <!--指标来源-->
                                    <el-select v-model="formData.indisource">
                                        <el-option v-for="item in indiSourceOpt"
                                                   :key="item.value"
                                                   :label="item.label"
                                                   :value="item.value">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-row>
                            <el-row>
                                <el-form-item style="width: 100%">
                                    <!-- 指标来源为指标下达 和 hr统一分配 时不需要员工进行指标填报 故隐藏这一行 -->
                                    <el-checkbox v-if="false"
                                                 v-model="formData.isallowfillup"
                                                 :disabled="!!formData.id">
                                        <!--员工进行指标填报-->{{_sn('pm.i18n1361')}}</el-checkbox>
                                    <el-checkbox v-model="formData.isresultaudited">
                                        <!--考核结果 需要 审核-->{{_sn('pm.i18n383')}}</el-checkbox>
                                    <el-checkbox v-model="formData.isresultconfirm">
                                        <!--结果 需员工确认-->
                                        {{'结果需员工确认'}}
                                    </el-checkbox>
                                    <el-checkbox :disabled="isjiazhiguan"
                                                 v-model="formData.isallowmtr">
                                        <!--支持中期回顾-->
                                        {{'支持中期回顾'}}
                                    </el-checkbox>
                                    <el-checkbox v-model="formData.isturn">
                                        <!--按轮次顺序打分-->{{_sn('pm.i18n384')}}</el-checkbox>
                                    <el-checkbox v-model="formData.ismergemsg">
                                        <!--前面多人时只收到最后一条消息-->{{_sn('pm.i18n738')}}</el-checkbox>
                                    <el-checkbox v-model="formData.isdoubleweight">
                                        <!--按指标分类、指标两级设置权重-->{{_sn('pm.i18n1280')}}</el-checkbox>
                                </el-form-item>
                            </el-row>
                            <el-row>
                                <el-form-item v-if="tFlag">
                                    <p @click="onMoreSetFun"
                                       style="color:#007ACE;cursor:pointer">{{_sn('pm.i18n1362')}}</p>
                                    <!--更多设置-->
                                </el-form-item>
                            </el-row>
                            <el-row>
                                <el-form-item :label="_sn('pm.i18n150')"
                                              prop="remark"
                                              style="width:100%;max-width: 880px">
                                    <!--方案说明-->
                                    <mix-textarea resize="none"
                                                  type="textarea"
                                                  :rows="3"
                                                  :maxlength="200"
                                                  v-model="formData.remark"></mix-textarea>
                                </el-form-item>
                            </el-row>
                        </el-form>
                    </div>
                </el-tab-pane>
                <el-tab-pane :label="_sn('common.i18n77')"
                             name="1"
                             :disabled="true">
                    <!--考核流程-->
                    <!--<p @click="addItem(-1, formData.oevaAppraiser)">+ 添加轮次{{_sn('pm.i18n166')}}</p>-->
                    <el-form>
                        <el-table border
                                  :data="oevaAppraiser"
                                  class="table-apprasier border-table"
                                  style="width:100%"
                                  :max-height="tableHeight"
                                  :height="tableHeight">
                            <div slot="empty"
                                 v-show="oevaAppraiser.length === 0 && !editFlag"
                                 @click="addItem(-1, oevaAppraiser)">
                                +
                                <!--新建流程-->
                                {{_sn('pm.i18n241')}}
                            </div>
                            <div slot="empty"
                                 v-show="oevaAppraiser.length === 0 && editFlag">
                                <sn-empty class="add-empty"></sn-empty>
                            </div>
                            <el-table-column :label="_sn('common.i18n49')"
                                             prop="turn">
                                <!--轮次-->
                                <template slot-scope="scope">
                                    <el-form-item>
                                        <!--<span>第</span>-->
                                        <span>{{_sn('common.i18n1000')[scope.row.turn -1]}}</span>
                                        <span>
                                            {{_sn('pm.i18n901')}}
                                            <!--第 xx 轮-->
                                        </span>
                                        <!--<el-input  v-model="scope.row.turn" >-->
                                        <!--</el-input>-->
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column prop="dimname"
                                             :label="_sn('common.i18n70')">
                                <!--考核人类别-->
                                <!--必填-->
                                <template slot-scope="scope">
                                    <el-form-item
                                                  :class="{'is-error': scope.row.checking==='checking'&&!scope.row.dimid}">
                                        <el-select v-model="scope.row.dimid"
                                                   placeholder="* "
                                                   class="is-required">
                                            <el-option v-for="item in dimoptions"
                                                       :key="item.id"
                                                       :label="dimoptionMap[item.dimcode] || item.dimname"
                                                       :value="item.id"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column :label="_sn('pm.i18n81')"
                                             prop="weight">
                                <!--考核权重-->
                                <!--必填-->
                                <template slot-scope="scope">
                                    <el-form-item
                                                  :class="{'is-error': scope.row.checking==='checking'&&! (scope.row.weight >= 0)}">
                                        <el-input v-model="scope.row.weight"
                                                  placeholder="*"
                                                  :max="100"
                                                  :min="0"
                                                  :controls="false"
                                                  :class="{'is-required': !scope.row.weight && scope.row.weight !==0 }"
                                                  style="width: 60px;display: inline-block"></el-input>
                                        <i style="font-size: 14px; margin-right: 5px;display: inline-block">%</i>

                                        <!--<i style="font-size: 14px; margin-right: 5px;position: absolute;left:160px;">%</i>-->
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column :label="_sn('pm.i18n106')">
                                <!--指定考核人-->
                                <template slot-scope="scope">
                                    <el-form-item>
                                        <Ref :placeholder="_sn('common.i18n7')"
                                             :model="scope.row.examiner"
                                             field="refpk"
                                             labelField="refname"
                                             refCode="staff_tree_grid_ref"
                                             :refParam="{refTitle: _sn('common.i18n31')}"
                                             :clientParam="{orgid: formData.orgid}"
                                             refTo="ref-wrap"></Ref>
                                        <!--考核人-->
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column v-if="this.formData.schememode !== 4">
                                <template slot-scope="scope">
                                    <el-form-item>
                                        <el-checkbox v-model="scope.row.isapprove">
                                            <!--指标审核-->
                                            {{_sn('pm.i18n66')}}
                                        </el-checkbox>
                                    </el-form-item>
                                </template>
                            </el-table-column>
                            <el-table-column width="180"
                                             align="right">
                                <template slot-scope="scope">
                                    <i class="iconfont wui-Add_bold hover-icon"
                                       v-show="!editFlag"
                                       @click="addItem(scope.$index, oevaAppraiser)"></i>
                                    <i class="iconfont wui-Dustbin_bold hover-icon"
                                       v-show="!editFlag"
                                       @click="delItem(scope.$index, oevaAppraiser)"></i>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane :label="_sn('common.i18n79')"
                             name="2"
                             :disabled="true">
                    <!--考核对象-->
                    <listTable :listData="ogroups"
                               :tableData="tableData"
                               :indisource="formData.indisource"
                               ref="refobject"
                               :schemeid="programID||formData.id"
                               :tableHeight="tableHeight - 46"
                               @addGroup="addGroup"
                               @saveObject="saveObject"
                               @afterObject="afterObject"
                               @afterObjectOrg="afterObjectOrg"
                               @afterRefGauge="afterRefGauge"
                               @choose-item="chooseItem"
                               @edit="add"
                               @updateGroup="updateGroup"
                               :editFlag="editFlag"
                               :refParams="refParams"
                               @toAllocation="toAllocation"></listTable>
                </el-tab-pane>
            </el-tabs>
        </div>
        <el-dialog :title="_sn('pm.i18n245')"
                   :visible.sync="dialogVisible"
                   :close-on-click-modal="false"
                   class="obj-dialog"
                   width="70%">
            <!--新建考核对象组-->
            <el-row :gutter="10">
                <el-col :span="isNcc ? 8 : 6">
                    <Ref :model="model.group"
                         field="refpk"
                         refCode="org_ref"
                         :refParam="{refTitle: _sn('pm.i18n247'),multi:true,diaWidth: '70%'}"
                         :clientParam="{serviceCode: 'HRPM0010'}"
                         labelField="refname"
                         placeholder
                         refTo="ref-wrap"
                         :customize="true"
                         @afterReference="saveGroup(1)">
                        <div class="setting-item">
                            <div class="title">
                                <!--按部门生成组-->
                                {{_sn('pm.i18n246')}}
                            </div>
                            <p>
                                <!--选择几个部门，部门的人员会自动生成组，组以部门命名。-->
                                {{_sn('pm.i18n248')}}
                            </p>
                            <div class="icon-container">
                                <i class="iconfont wui-anbumenx"></i>
                            </div>
                        </div>
                    </Ref>
                    <!--选择部门-->
                </el-col>
                <el-col :span="isNcc ? 8 : 6">
                    <Ref :model="model.group"
                         field="refpk"
                         refCode="post_tree_grid_ref"
                         :refParam="{refTitle: _sn('pm.i18n250'),multi:true,diaWidth: '70%'}"
                         :clientParam="suporiorParam"
                         labelField="refname"
                         placeholder
                         :customize="true"
                         @afterReference="saveGroup(2)"
                         refTo="ref-wrap">
                        <div class="setting-item">
                            <div class="title">
                                <!--按职位生成组-->
                                {{_sn('pm.i18n249')}}
                            </div>
                            <p>
                                <!--选择几个职位，职位的人员会自动生成组，组以职位命名。-->
                                {{_sn('pm.i18n251')}}
                            </p>
                            <div class="icon-container">
                                <i class="iconfont wui-angangweix"></i>
                            </div>
                        </div>
                    </Ref>
                    <!--选择职位-->
                </el-col>
                <el-col :span="isNcc ? 8 : 6">
                    <Ref :model="model.group"
                         field="refpk"
                         refCode="staff_tree_grid_ref"
                         :refParam="{refTitle: _sn('pm.i18n253'),multi:true,diaWidth: '70%',customTableHeaders: [
                     {prop: 'refname', label: _sn('common.i18n4')},
                     {prop: 'refcode', label: _sn('common.i18n11')},
                     {prop: 'deptName', label: _sn('common.i18n20')}
                   ]}"
                         :clientParam="{serviceCode: 'HRPM0010', depttype: -1}"
                         labelField="refname"
                         @afterReference="saveGroup(3)"
                         placeholder
                         :customize="true"
                         refTo="ref-wrap">
                        <div class="setting-item">
                            <div class="title">
                                <!--按成员生成组-->
                                {{_sn('pm.i18n252')}}
                            </div>
                            <p>
                                <!--多选人员建成分组-->
                                {{_sn('pm.i18n254')}}
                            </p>
                            <div class="icon-container">
                                <i class="iconfont wui-anchengyuan"></i>
                            </div>
                        </div>
                    </Ref>
                    <!--选择人员-->
                </el-col>
                <el-col v-if="!isNcc">
                    <Ref :model="model.group"
                         field="refpk"
                         refCode="pm_project_grid_ref"
                         :refParam="{refTitle: _sn('pm.i18n740'),multi:true,diaWidth: '70%',
             customTableHeaders:[
                     {prop: 'no',label: _sn('common.i18n16')},
                     {prop: 'refname',label: _sn('pm.i18n741')},
                     {prop: 'date',label: _sn('pm.i18n742')},
                     {prop: 'psnname',label: _sn('pm.i18n743')},
                     {prop: 'psncount',label: _sn('pm.i18n744')}
                   ]}"
                         :clientParam="{condition: formData.periodid}"
                         labelField="refname"
                         @afterReference="saveGroup"
                         placeholder
                         :customize="true"
                         refTo="ref-wrap">
                        <div class="setting-item">
                            <div class="title">
                                <!--按项目生成组-->
                                {{_sn('pm.i18n739')}}
                            </div>
                            <p>
                                <!--多选项目建成分组-->
                                {{_sn('pm.i18n745')}}
                            </p>
                            <div class="icon-container">
                                <i class="iconfont wui-Comment"></i>
                            </div>
                        </div>
                    </Ref>
                    <!--选择项目-->
                    <!--header 序号 项目名称 项目起止时间 项目成员 项目人数-->
                </el-col>
            </el-row>
        </el-dialog>
        <!-- popup:更多设置 -->
        <more-setting v-if="tFlag"
                      ref="moreSet"></more-setting>
        <div class="pro-operation basic-back">
            <!--{{typeof activeName}} {{activeName}}-->
            <div v-if="!formData.isresultaudited && activeStep === 2">
                <!--定义没有选择结果审核设置时第三步成为最后一步，直接设置完成或者提交-->
                <el-button type="primary"
                           @click="saveF('')">
                    {{_sn('pm.i18n142')}}
                    <!--设置完成-->
                </el-button>
                <el-button type="primary"
                           @click="saveF('submit')">
                    {{_sn('pm.i18n1128')}}
                    <!--启动方案-->
                </el-button>
                <el-button type="text"
                           @click="$router.go(-1)">
                    {{_sn('pm.i18n121')}}
                    <!--关闭-->
                </el-button>
            </div>
            <div v-else>
                <!--<el-button type="info" v-if="(tFlag||yFlag)&&activeStep===0&&formData.id" @click="copyScheme">复制为新方案复制为新方案</el-button>-->
                <el-button type="primary"
                           v-if="(!activeStep&&!programID) || (!activeStep&&isMiddle)"
                           @click="save">
                    {{_sn('pm.i18n1109')}}
                    <!--创建-->
                </el-button>
                <el-button type="primary"
                           @click="save"
                           v-else>
                    {{_sn('common.i18n3')}}
                    <!--保存-->
                </el-button>
                <el-button type="text"
                           @click="$router.push({name: 'pm-program'})">
                    {{_sn('pm.i18n121')}}
                    <!--关闭-->
                </el-button>
            </div>
        </div>
        <is-adopt-dialog ref="isAdopt"
                         :getData="callBack"></is-adopt-dialog>
    </div>
</template>
<script>
import { fetchData } from '@/utils/util.js'
import HeaderWrap from '@/components/common/header-wrap'
import listTable from './listTable'
import Ref from '@/components/controls/reference'
import MixTextarea from '@/components/common/mix-textarea'
import Progress from '@/components/common/progress/progress'
import MoreSetting from './modules/moreSetting'
import middleView from './modules/middleView'
import IsAdoptDialog from './modules/isAdoptDialog'
import gauge from '@/pages/pm/mixins/gauge.js'
import getCurrentContext from '@/mixins/getCurrentContext'
import emitter from '@/mixins/emitter'
import { dimoptionMap, applyobjOption } from '../common/preset-data.js'
const { performance } = window.ServiceConfigs

export default {
    name: 'Pmedit',
    mixins: [gauge, getCurrentContext, emitter],
    components: {
        Progress,
        MixTextarea,
        Ref,
        listTable,
        HeaderWrap,
        MoreSetting,
        middleView,
        IsAdoptDialog
    },
    props: {
        addFlag: {
            type: Boolean,
            default: true
        }
    },
    watch: {
        '$route' (to, from) {
            if (to.name === 'person' && from.params.showTarget) {
                this.showTarget = false
            }
        }
    },
    data: function () {
        return {
            isMiddle: false,
            showTargetPannel: false,
            isjiazhiguan: false,
            showMoreTags: false, // 是否显示全部方案类型的标签
            showAddTag: false, // 是否显示添加标签输入框
            addTag: '', // 添加标签
            tagsList: [], // 方案类型的标签数组
            oldSchemeId: '', // 为复制方案时-旧方案的id(后端配合)
            listData: [],
            editFlag: false,
            dialogVisible: false,
            activeGroup: '',
            activeIndex: '',
            programID: '',
            oevaAppraiser: [],
            oevaAppraiserBase: [],
            formData: {
                oevaAppraiser: [
                ],
                isallowmtr: false, // 是否支持中期回顾 false不支持
                orgid: '',
                schememode: 0, // 0 PBC 1 360 TODO
                // schemecode: '000',
                scoreway: '',
                isallowcalc: false, // 是否支持指标评分
                schemename: '',
                periodid: '',
                periodcode: '',
                isallowfillup: true,
                isresultaudited: true,
                isturn: true,
                ismergemsg: false,
                remark: '',
                scoreruleid: '',
                applyobj: 1,
                tagid: '', // 方案类型标签id
                // tagname: '' // 方案类型标签name
                isresultconfirm: false // 结果是否需要员工确认
            },
            formDataBase: {},
            loading: false,
            tableData: [],
            model: {
                scorerule: {
                    scoreruleid: '',
                    scorerulename: ''
                },
                period: {
                    code: '',
                    periodid: '',
                    periodname: ''
                },
                group: {

                },
                staff: {
                    refpk: '',
                    refname: ''
                },
                examiner: {
                    refpk: '',
                    refname: ''
                },
                org: {
                    refpk: '',
                    refname: ''
                },
                indi: {
                    refpk: '',
                    refname: ''
                },
                indiup: {
                    refpk: '',
                    refname: ''
                },
                indinum: {
                    refpk: '',
                    refname: ''
                },
                indisame: {
                    refpk: '',
                    refname: ''
                }
            },
            options: [
                {
                    label: '0',
                    value: 0
                },
                {
                    label: '1',
                    value: 1
                },
                {
                    label: '2',
                    value: 2
                },
                {
                    label: '3',
                    value: 3
                }
            ],
            dimoptionMap: dimoptionMap, // 人员关系预置数据 上级 下级。。。
            dimoptions: [],
            rules0: {
                orgid: [
                    { required: true, message: this._sn('pm.i18n952') }
                ], // <!--请选择组织或部门-->
                schemename: [
                    { required: true, message: this._sn('pm.i18n746') },
                    { max: 40, message: this._sn('common.i18n33'), trigger: ['blur', 'change'] }
                ], // <!--请输入方案名称  不得超过40字-->
                periodid: [
                    { required: true, message: this._sn('pm.i18n256') }
                ], // <!--请输入绩效期间-->
                remark: [
                    { max: 200, message: this._sn('common.i18n39'), trigger: 'blur' }
                ], // <!--不得超过200字-->
                scoreruleid: [
                    { required: true, message: this._sn('pm.i18n257') }
                ], // 请输入指标评分规则
                gradeid: [
                    { required: true, message: this._sn('pm.i18n258') }
                ], // 请输入考核结果等级
                applyobj: [
                    { required: true, message: '请选择考核对象类型' }
                ], // 请选择考核对象类型
                scoreway: [
                    { required: true, message: this._sn('pm.i18n259') }
                ] // 请输入总分汇总规则
            },
            rules1: {
                name: [
                    { required: true, message: this._sn('common.i18n18') },
                    { max: 40, message: this._sn('common.i18n33'), trigger: 'blur' }
                    // { min: 3, max: 5, message: '<!--长度在 3 到 5 个字符-->{{_sn('common.i18n47')}}' }
                ], // 请输入名称 不得超过40字
                remark: [
                    { max: 200, message: this._sn('common.i18n39'), trigger: 'blur' }
                ] // <!--不得超过200字-->
            },
            activeName: '0',
            indiSourceOpt: [
                {
                    label: this._sn('pm.i18n1571'), // 员工填报
                    value: 0
                },
                {
                    label: this._sn('pm.i18n1572'), // HR统一分配
                    value: 1
                },
                {
                    label: this._sn('pm.i18n1573'), // 上级下达
                    value: 2
                }
            ],
            scorewayopt: [
                {
                    label: this._sn('pm.i18n260'), // <!--加权求和-->
                    value: 1
                },
                {
                    label: this._sn('pm.i18n261'), // <!--算术求和-->
                    value: 2
                },
                {
                    label: '算术平均', // <!--算术平均-->
                    value: 3
                }
            ],
            gradeopt: [],
            ogroups: [],
            headData: [
                {
                    name: this._sn('common.i18n24'),
                    value: 'staff.name',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '80'
                }, // <!--姓名-->
                {
                    name: this._sn('pm.i18n1286'),
                    value: 'staff.corpname',
                    width: '120'
                }, // <!--任职组织-->
                {
                    name: this._sn('common.i18n20'),
                    value: 'staff.deptname',
                    required: true,
                    type: 'ref',
                    width: '130'
                }, // <!--部门-->
                {
                    name: this._sn('common.i18n73'), //  <!--职位-->
                    value: 'staff.postname'
                },
                // {
                //   name: this._sn('common.i18n64'),
                //   value: 'staff.jobname'
                // }, // <!--职务-->
                {
                    name: this._sn('common.i18n28'),
                    value: 'staff.psnclname'
                } // <!--员工类别-->
            ],
            headAppraiser: {
                turn: {// <!--必填-->
                    name: this._sn('common.i18n49'), // <!--轮次-->
                    required: true
                },
                dimid: {// <!--必填-->
                    name: this._sn('common.i18n70'), // <!--考核人类别-->
                    required: true
                },
                weight: {// <!--必填-->
                    name: this._sn('pm.i18n81'),
                    required: true
                },
                staffid: {
                    name: this._sn('pm.i18n106')
                } // <!--指定考核人-->
            },
            valid: false,
            stepArr: [
                this._sn('pm.i18n1104'), this._sn('pm.i18n1105'), this._sn('pm.i18n1106'), this._sn('pm.i18n1107')
            ], // 创建方案，设置考核流程，设置考核对象，设置结果审核流程
            stepArr2: [
                this._sn('pm.i18n1104'), this._sn('pm.i18n1105'), this._sn('pm.i18n1106')
            ], // 创建方案，设置考核流程，设置考核对象

            // 设置考核流程 设置考核对象 设置结果审核流程
            activeStep: 0,
            tableHeight: 450,
            applyobjOption: applyobjOption, // 考核对象类型预置数据 1人员 2组织
            refParams: {}
        }
    },
    computed: {
        suporiorParam () {
            return {
                // orgId: this.formData.orgid,
                serviceCode: 'HRPM0010',
                depttype: -1
            }
        }
    },
    created: function () {
        // this.editFlag = false
        this.programID = this.$route.query.programId ? this.$route.query.programId : ''
        this.oldSchemeId = this.$route.query.oldSchemeId ? this.$route.query.oldSchemeId : ''
        this.from = this.$route.query.from || ''
        if (this.from === 'middle') {
            this.isMiddle = true
        }
        this.activeStep = parseInt(this.$route.query.browser)
        this.activeName = this.activeStep.toString()
        if (this.isNcc) {
            // ncc开放所有的租户
            this.showTargetPannel = true
        } else {
            // 针对租户iqopszg1（workbench）、rpmdvpqp(蓝海)、cxsv1gjz、jmr2niqz（diwork-daily人才绩效专用）、qyic8c7o（用友集团）、dhzobw6v（2020绩效管理人才盘点），可以开放标签，其他租户都不显示
            let _tenantid = window.$diworkContext.tenantid
            if (_tenantid === 'iqopszg1' ||
                _tenantid === 'rpmdvpqp' ||
                _tenantid === 'cxsv1gjz' ||
                _tenantid === 'jmr2niqz' ||
                _tenantid === 'qyic8c7o' ||
                _tenantid === 'dhzobw6v') {
                this.showTargetPannel = true
            }
        }
        // this.editable = this.$route.params.editable
        if (this.programID) {
            this.getData()
        } else {
            if (this.oldSchemeId) {
                this.copyIdFun()
            }
            this.getGrade()
            this.getTagsList()
        }
        this.getDim() // <!--指标分类-->
        // if (this.$route.query.browser) {
        // this.editFlag = true TODO 此页面专为编辑态提供
        // } else {
        // this.editFlag = false
        // }
    },
    updated: function () {
        this.$nextTick(function () {
            // this.tableHeight = this.$refs.content.offsetHeight - 15
            this.tableHeight = document.documentElement.clientHeight - 65 - 24 - 60
            // debugger
            let _this = this
            window.onresize = function () {
                _this.tableHeight = document.documentElement.clientHeight - 65 - 24 - 60
            }
            window.hhh = this
        })
    },
    methods: {
        getCookie (cookieName) {
            // 获取所有的cookie "psw=1234we; rememberme=true; user=Annie"
            let totalCookie = document.cookie
            // 获取参数所在的位置
            let cookieStartAt = totalCookie.indexOf(cookieName + '=')
            // 判断参数是否存在 不存在直接返回
            if (cookieStartAt === -1) {
                return
            }
            // 获取参数值的开始位置
            let valueStartAt = totalCookie.indexOf('=', cookieStartAt) + 1
            // 以;来获取参数值的结束位置
            let valueEndAt = totalCookie.indexOf(';', cookieStartAt)
            // 如果没有;则是最后一位
            if (valueEndAt === -1) {
                valueEndAt = totalCookie.length
            }
            // 截取参数值的字符串
            let cookieValue = unescape(totalCookie.substring(valueStartAt, valueEndAt))
            return cookieValue
        },
        /**
         * 标签选中事件
         */
        selectTag (index) {
            this.tagsList.map(item => item.isSelect = false)
            this.tagsList[index].isSelect = true
            this.formData.tagid = this.tagsList[index].id
            // 如果选择了价值观考核 就不选中员工进行指标填报
            if (this.tagsList[index].name === '价值观考核') {
                this.formData.isallowfillup = false
                this.isjiazhiguan = true
            } else {
                // 这里不改变员工填报的状态，因为要控制住二次修改时不可改变
                // this.formData.isallowfillup = true
                this.isjiazhiguan = false
            }
        },
        /**
         * 添加标签事件
         */
        onTagAdd () {
            if (this.addTag.trim().length === 0) {
                return false
            }
            let _index = this.tagsList.findIndex(item => item.name === this.addTag.trim())
            if (_index > -1) {
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: '不可重复创建！'
                })
                return false
            }
            let _data = {
                tagname: this.addTag,
                tagtype: 'Scheme'
            }
            fetchData(`${window.hrPath}${performance.core}/pm/base/addTag`, {                type: 'POST',
                data: _data
            }).then(res => {
                if (res.statusCode === 200) {
                    res.data.isSelect = false
                    this.tagsList.push(res.data)
                    this.showAddTag = false
                    // 如果新添加的标签过长 则展开全部，防止用户看不到以为没有添加成功
                    if (this.tagsList.length > 3 && this.addTag.length >= 4) {
                        this.showMoreTags = true
                    }
                    // 使用完后清空标签
                    this.addTag = ''
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            }).catch((err) => {
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
            })
        },
        /**
         * 获取系统考核类型标签
         * 
         */
        getTagsList () {
            fetchData(`${window.hrPath}${performance.core}/pm/base/queryTagList`, {
                type: 'GET',
                data: {
                    tagtype: 'Scheme'
                }
            }).then(res => {
                if (res.statusCode === 200) {
                    res.data.map(item => {
                        item.isSelect = false
                        // 如果实在修改状态下
                        if (this.formData.tagid) {
                            item.isSelect = this.formData.tagid === item.id
                            //   如果是中期回顾的子方案 - 只显示浏览态的标签名称
                            // this.formData.schememode === 20 为中期回顾子方案的浏览态
                            // this.isMiddle === true 为中期回顾子方案的创建态
                            if ((this.formData.schememode === 20 || this.isMiddle) && (this.formData.tagid === item.id)) {
                                this.formData.tagname = item.name
                            }
                        }
                    })
                    // 如果不是修改，那就默认选中第一个
                    if (!this.formData.tagid) {
                        res.data[0].isSelect = true
                        this.formData.tagid = res.data[0].id
                    }
                    this.tagsList = res.data
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            }).catch((err) => {
                console.log('err', err)
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
            })
        },
        fetchData,
        afterOrg (org) {
            this.formData.orgid = org[0] ? org[0].refpk : ''
            this.getGrade()
        },
        afterReference (data) {
            this.formData.periodid = data[0] ? data[0].refpk : ''
            this.formData.periodcode = data[0] ? data[0].refcode : ''
        },
        afterScore (data) {
            this.formData.scoreruleid = data[0] ? data[0].refpk : ''
            // this.formData.periodcode = data[0].refcode
        },
        afterObject (data, activeid) {
            // <!--添加组员 保存考核对象-->
            let _data = []
            data.forEach((k, i) => {
                let idata = { staffid: '', groupid: '', schemeid: this.formData.id || '' } // , staff: {}
                idata.staffid = k.refpk
                idata.groupid = this.activeGroup
                _data.push(idata)
            })
            fetchData(`${window.hrPath}${performance.core}/pm/system/saveEvaObject`, {
                type: 'Post',
                data: _data
            }).then(res => {
                if (res.statusCode === 200) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    })
                    this.chooseItem(activeid)
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
                this.$refs.refobject.model = { staffid: '', refname: '' }
            }).catch((err) => {
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                this.$refs.refobject.model = { staffid: '', refname: '' }
            })
        },
        afterObjectOrg (data, activeid) {
            // <!--添加组织 保存考核对象-->
            let _data = []
            // debugger
            data.forEach((k, i) => {
                let idata = { orgid: '', groupid: '', schemeid: this.formData.id || '' } // , staff: {}
                idata.orgid = k.refpk
                idata.groupid = this.activeGroup
                _data.push(idata)
            })
            fetchData(`${window.hrPath}${performance.core}/pm/system/saveEvaObjectForOrg`, {
                type: 'Post',
                data: _data
            }).then(res => {
                if (res.statusCode === 200) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    })
                    this.chooseItem(activeid)
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
                this.$refs.refobject.model = { staffid: '', refname: '' }
            }).catch((err) => {
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                this.$refs.refobject.model = { staffid: '', refname: '' }
            })
        },

        change () {
            /* eslint-disable  no-undef */
            let waringicon = '<i class="iconfont wui-Warning" ></i>'
            $('div.el-form-item__error').before(waringicon)
        },
        getGrade (id) {
            let data = id ? encodeURIComponent(JSON.stringify({ 'enablestate': true, 'orgid': id })) : encodeURIComponent(JSON.stringify({ 'enablestate': true, 'orgid': this.formData.orgid }))
            fetchData(`${window.hrPath}${performance.core}/pm/base/queryGradeRef?cond=` + data).then(data => {
                if (data.statusCode === 200) {
                    this.gradeopt = data.data
                    let periodFlag = 0
                    this.gradeopt.forEach(item => {
                        if (this.formData.gradeid === item.id) {
                            periodFlag++
                        }
                    })
                    if (periodFlag === 0) {
                        data.data.gradeid = ''
                    }
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: data.message
                    })
                }
            })
        },
        getDim () {
            let param = encodeURIComponent(JSON.stringify({ 'enablestate': true }))
            fetchData(`${window.hrPath}${performance.core}/pm/base/queryDimByConditon?cond=` + param).then(data => {
                if (data.statusCode === 200) {
                    this.dimoptions = data.data
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: data.message
                    })
                }
            })
        },
        getData () {
            this.loading = true
            let o = this
            fetchData(`${window.hrPath}${performance.core}/pm/system/querySchemeByID?id=` + this.programID).then(data => {
                if (data.statusCode === 200) {
                    this.loading = false
                    data.data && o.handleData(data.data)
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: data.message
                    })
                    this.loading = false
                }
            }).catch(err => {
                this.loading = false
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: err.message
                })
            })
        },
        handleData (obj) {
            obj.tagname = ''
            this.formData = obj
            if (this.isMiddle && this.activeStep === 0) {
                // 中期回顾的方案 - 没有第四步
                this.formData.isresultaudited = false
                this.formData.mainschemeid = this.formData.id
                this.formData.id = ''
                if (!this.activeStep) {
                    this.formData.schemename = this.formData.schemename + '_中期回顾'
                }
            }
            this.getGrade(obj.orgid) // <!--考核结果等级-->
            this.formDataBase = JSON.parse(JSON.stringify(this.formData))
            this.ogroups = obj.ogroups
            this.ogroups.forEach(item => {
                item.groupFlag = true
            })
            this.activeGroup = this.ogroups[0] ? this.ogroups[0].id : ''
            this.oevaAppraiser = obj.oevaAppraiser
            this.oevaAppraiser.forEach(item => {
                item.examiner = {
                    'refpk': item.staffid,
                    'refname': item.staffname
                }
                item.checking = ''
                // item.isapprove = false // 考核结果核算 非必填
                // item.schemeid = 'a33eb4d2bdd64224a7abcb9b2febc56e' // <!--必填-->{{_sn('common.i18n41')}}
                item.weight = parseInt(item.weight * 100)
            })
            this.oevaAppraiserBase = JSON.parse(JSON.stringify(this.oevaAppraiser))
            this.model.period.periodname = obj.periodname
            this.model.period.periodid = obj.periodid
            this.model.scorerule.scorerulename = obj.scorerulename
            this.model.scorerule.scoreruleid = obj.scoreruleid
            this.$nextTick(() => {
                this.refParams = {
                    isdoubleweight: obj.isdoubleweight, // 按指标分类、指标两级设置权重
                    isallowfillup: obj.isallowfillup, // 是否需要员工填报
                    applyobj: obj.applyobj // 适用对象类型
                }
            })
            this.model.org.refpk = obj.orgid
            this.getTagsList()
        },
        addItem (index, rows) {
            let data = {
                examiner: {
                    'refpk': '',
                    'refname': ''
                },
                isapprove: false,
                dimid: '',
                turn: index + 2
            }
            // 可以不填写的情况下先添加多行
            rows.splice(index + 1, 0, data)
            rows.forEach((item, i) => {
                if (i > index) {
                    item.turn = i + 1
                }
            })
        },
        delItem (index, rows) {
            rows.splice(index, 1)
            rows.forEach((item, index) => {
                item.turn = index + 1
            })
        },
        del () {
            fetchData(`${window.hrPath}${performance.core}/pm/base/delScoreRule`, {
                type: 'Post',
                data: { id: this.formData.id }
            }).then(res => {
                this.$message({
                    type: 'success',
                    message: res.message
                })
                this.$emit('init')
            })
        },
        add () {

        },
        addGroup () {
            if (this.formData.applyobj === 2) {
                this.saveGroup(5)
            } else {
                this.dialogVisible = true
            }
        },
        chooseItem (data, key) {
            this.activeGroup = data
            if (key === 5) {
                this.$refs.refobject.change()
            }
            this.ogroups.forEach(item => {
                if (item.id === data) {
                    fetchData(`${window.hrPath}${performance.core}/pm/system/queryEvaGroupByID`, {
                        data: { id: item.id }
                    }).then(res => {
                        this.tableData = res.data.oevaObjects
                    }).catch((err) => {
                        this.$message({
                            type: 'error',
                            duration: 5000,
                            message: err
                        })
                    })
                    // this.tableData = item.ogradeItem
                    // this.formData = item
                }
            })
        },
        check () {
            if (this.formData.schememode === 20) {
                return this.$refs.middleView.check()
            } else {
                let BreakException = '方案定义填写不完整'
                // 数据不完整this._sn('pm.i18n189')
                try {
                    this.$refs['formData'].validate((valid) => {
                        if (valid) {
                        } else {
                            throw BreakException
                        }
                    })
                    return true
                } catch (err) {
                    return BreakException
                }
            }
        },
        submit (schemeid) {
            this.loading = true
            let _this = this
            // let flag = this.save('submit')
            let data = { id: schemeid || this.formData.id }
            // if (flag) {
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/submitScheme`, {
                type: 'Post',
                data
            }).then(res => {
                this.loading = false
                if (res.statusCode === 200) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    })
                    _this.$router.go(-2)
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            }).catch(err => {
                this.loading = false
                // <!--服务器失联了，请稍后再试-->
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: this._sn('pm.i18n122')
                })
                throw new Error(err)
            })
            // }
        },
        checkAppraiser (data) {
            let BreakException = {}
            try {
                data.forEach(item => {
                    let reg = new RegExp(/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/)
                    if (!reg.test(item.weight) || item.weight > 100) {
                        // <!--流程权重必须为一百以内正数-->
                        BreakException = this._sn('pm.i18n269')
                        throw BreakException
                    }
                    let i = 0
                    let n = 0
                    for (var key in item) {
                        if (this.headAppraiser[key] && this.headAppraiser[key].required) {
                            n++
                            if (item[key] || item[key] === 0) {
                                i++
                            }
                        }
                    }
                    if (i < n) {
                        this.$set(item, 'checking', 'checking')
                        // <!--考核流程填写不完整-->
                        BreakException = this._sn('pm.i18n270')
                        throw BreakException
                    } else {
                        // item.weight = parseInt(item.weight) / 100
                        // throw BreakException = true
                    }
                    // this.$set(item[index], 'checking', 'checking')
                    // 不影响显示
                })
                return true
            } catch (error) {
                // this.$alert(error, {
                //   confirmButtonText: this._sn('common.i18n2'), // <!--确定-->
                //   confirmButtonClass: 'el-button--warning',
                //   type: 'warning',
                // duration: 5000
                // })
                return error
            }
        },
        save () {
            if (this.isMiddle && this.activeStep === 0) {
                this.$refs.middleView.createMiddlePro()
            } else {
                this.loading = true
                // this.formData.scoreruleid = this.model.scorerule.scoreruleid
                this.formData.orgid = this.model.org.refpk
                let flag = false
                let data = this.formData
                this.oevaAppraiser.forEach(item => {
                    item.staffid = item.examiner.refpk
                })
                // this.formData.periodid = this.model.period.periodid
                // this.formData.periodcode = this.model.period.code
                this.oevaAppraiserBase = JSON.parse(JSON.stringify(this.oevaAppraiser))
                this.oevaAppraiserBase.forEach(item => {
                    item.weight = item.weight ? parseInt(item.weight) / 100 : 0
                })
                data.oevaAppraiser = this.oevaAppraiserBase
                this.oevaAppraiser.forEach(item => {
                    item.staffid = item.examiner.refpk
                })
                this.oevaAppraiserBase = JSON.parse(JSON.stringify(this.oevaAppraiser))
                this.oevaAppraiserBase.forEach(item => {
                    item.weight = item.weight ? parseInt(item.weight) / 100 : 0
                })
                if (this.formData.id) {
                    // let appraFlag = this.checkAppraiser(this.oevaAppraiser)
                    flag = this.check()
                    if (this.formData.schememode !== 20) {
                        if (flag === true) {
                            flag = this.checkAppraiser(this.oevaAppraiser)
                        }
                        data.oevaAppraiser = this.oevaAppraiserBase
                    }
                } else {
                    flag = this.check()
                }
                if (flag === true) {
                    fetchData(`${window.hrPath}${performance.core}/pm/system/saveScheme`, {
                        type: 'Post',
                        data
                    }).then(res => {
                        this.loading = false
                        if (res.statusCode === 200) {
                            // this.$router.push({query: {programId: res.data.id}})
                            // if (submit) {
                            //   this.submit(res.data.id)
                            // } else {
                            //
                            this.$message({
                                type: 'success',
                                message: res.message
                            })
                            // }
                            // this.$router.go(-1)
                            if (this.activeStep === 2) {
                                this.$router.push({ name: 'Pmallocation', query: { schemeid: this.formData.id, editFlag: true } })
                            } else {
                                this.programID = res.data.id
                                this.getData()
                            }
                            this.activeStep++
                            this.activeName = this.activeStep.toString()
                            // if (this.$route.query.programId) {
                            // switch (this.activeStep) {
                            //   case 0:
                            //     this.activeStep = 1
                            //     this.activeName = '1'
                            //     break
                            //   case 1:
                            //     this.activeStep = 2
                            //     this.activeName = '2'
                            //     break
                            //     case 2:
                            //       break
                            //   default: break
                            // }
                            // } else {
                            // this.$router.go(-1)
                            // }
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    })
                } else {
                    // if (!flag && this.activeStep !== 2)
                    this.loading = false
                    // <!--数据不符合要求 this._sn('pm.i18n167')-->
                    this.$alert(flag || this._sn('pm.i18n167'), {
                        confirmButtonText: this._sn('common.i18n2'), // <!--确定-->
                        confirmButtonClass: 'el-button--warning',
                        type: 'warning'
                    })
                    // } else {
                    //   this.loading = false
                }
            }
            //
        },
        saveF (step) {
            console.log('step', step)
            this.loading = true
            this.formData.scoreruleid = this.model.scorerule.scoreruleid
            this.formData.orgid = this.model.org.refpk
            // let flag = false
            let data = this.formData
            this.oevaAppraiser.forEach(item => {
                item.staffid = item.examiner.refpk
            })
            // this.formData.periodid = this.model.period.periodid
            // this.formData.periodcode = this.model.period.code
            this.oevaAppraiserBase = JSON.parse(JSON.stringify(this.oevaAppraiser))
            this.oevaAppraiserBase.forEach(item => {
                item.weight = item.weight ? parseInt(item.weight) / 100 : 0
            })
            data.oevaAppraiser = this.oevaAppraiserBase
            // if (flag) {
            fetchData(`${window.hrPath}${performance.core}/pm/system/saveScheme`, {
                type: 'post',
                data
            }).then(res => {
                this.loading = false
                // this.formData = data.data
                if (res.statusCode === 200) {
                    if (step === 'submit') {
                        this.submitF()
                    } else {
                        this.getData()
                        this.loading = false
                        this.$message({
                            type: 'success',
                            message: res.message
                        })
                    }
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                    this.loading = false
                }
            }).catch(err => {
                this.loading = false
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: err.message
                })
            })
            // }
        },
        callBack () {
            this.$router.push({ name: 'pm-program' })
        },
        submitF (step) {
            this.$refs.isAdopt.diaShow(this.formData.id, 'startPro')
        },
        tabChange (active) {
            // if (!this.formData.id) {
            //   // <!--请先填写并保存方案定义-->
            //   this.$alert(this._sn('pm.i18n272'), {
            //     confirmButtonText: this._sn('common.i18n2'), // <!--确定-->
            //     confirmButtonClass: 'el-button--warning',
            //     type: 'warning',
            // duration: 5000
            //   })
            // } else {
            //   this.$refs['formData'].validate((valid) => {
            //     if (valid) {
            this.activeName = active
            //     } else {
            //       this.activeName = 'first'
            //     }
            //   })
            // }
        },
        handleClick (data) {
            // this.save()
            this.$refs['formData'].validate((valid) => {
                if (valid) {
                    //
                    // return true
                    // this.valid = true
                } else {
                    data.active = false
                    this.activeName = '0'
                }
            })
        },
        saveGroup (key) {
            let data = {}
            // data.id = this.model.group.refpk.split(',')
            data.schemeid = this.formData.id || ''
            if (typeof (key) !== 'number') {
                //
                data.projectname = this.model.group.refname.split(',')[0]
                data.id = ''
                key.forEach(item => {
                    data.id = data.id + item.psnid
                })
                data.id = data.id.split(',')
                data.type = 4
            } else {
                data.type = key
                if (key !== 5) {
                    data.id = this.model.group.refpk.split(',')
                }
            }
            fetchData(`${window.hrPath}${performance.core}/pm/system/addEvaGroup`, {
                type: 'Post',
                data
            }).then(res => {
                if (res.statusCode === 200) {
                    this.ogroups.unshift(res.data)
                    if (key === 5) {
                        // this.ogroups.unshift(res.data)
                        this.$nextTick(() => {
                            this.$refs.refobject.change(res.data.id)
                        })
                    } else {
                        // this.ogroups.unshift(res.data)
                        this.chooseItem(res.data.id)
                        // this.getData(this.formData.id)
                        this.dialogVisible = false
                    }
                    this.$message({
                        type: 'success',
                        message: res.message
                    })
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            })
            this.dialogVisible = false
            this.model.group = {}
        },
        saveObject () {
            // this.
        },
        toAllocation () {
            let flag = 0
            let flagdef = false
            let _this = this
            let base = this.formDataBase
            if (this.formData.schemename === base.schemename &&
                this.formData.periodid === base.periodid &&
                this.formData.scorerule === base.scorerule &&
                this.formData.gradeid === base.gradeid &&
                this.formData.scoreway === base.scoreway &&
                this.formData.isresultaudited === base.isresultaudited &&
                this.formData.isturn === base.isturn &&
                this.formData.remark === base.remark) {
                flagdef = true
            }
            if (this.oevaAppraiser.length === _this.oevaAppraiserBase.length) {
                this.oevaAppraiser.forEach((item, i) => {
                    if (item.turn === _this.oevaAppraiserBase[i].turn &&
                        item.dimid === _this.oevaAppraiserBase[i].dimid &&
                        item.weight === _this.oevaAppraiserBase[i].weight &&
                        item.staffid === _this.oevaAppraiserBase[i].staffid &&
                        item.isapprove === _this.oevaAppraiserBase[i].isapprove) {
                        flag++
                    }
                })
            }
            if (flagdef && flag === this.oevaAppraiser.length) {
                this.$router.push({ name: 'Pmallocation', query: { schemeid: this.formData.id, editFlag: this.editFlag } })
            } else {
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: this._sn('pm.i18n747')
                }) // <!--请先保存修改-->
            }
        },
        copyScheme () {
            this.loading = true
            this.programID = ''
            this.formData = {
                oevaAppraiser: [],
                orgid: this.formData.orgid,
                orgname: this.formData.orgname,
                schememode: 0, // 0 PBC 1 360 TODO
                scoreway: this.formData.scoreway,
                schemename: this.formData.schemename,
                periodid: this.formData.periodid,
                periodcode: this.formData.periodcode,
                gradeid: this.formData.gradeid,
                gradename: this.formData.gradename,
                isresultaudited: this.formData.isresultaudited,
                isturn: this.formData.isturn,
                isallowfillup: this.formData.isallowfillup,
                ismergemsg: this.formData.ismergemsg,
                isdoubleweight: this.formData.isdoubleweight,
                remark: this.formData.remark,
                scoreruleid: this.formData.scoreruleid,
                scorerulename: this.formData.scorerulename,
                applyobj: this.formData.applyobj
            }
            this.formDataBase = JSON.parse(JSON.stringify(this.formData))
            this.oevaAppraiser = []
            this.oevaAppraiserBase = []
            this.tableData = [] // 考核对象table
            this.loading = false
            this.$message({
                type: 'success',
                message: '复制成功'
            })
        },
        afterRefGauge (data, lists) {
            this.tableData = this.tableData.map(item => {
                if (lists.indexOf(item.id) > -1) {
                    item.gaugeid = data[0] ? data[0].refpk : ''
                    item.gauge = data[0] ? data[0].refname : ''
                }
                return item
            })
        },
        chooseProgress (index) {
            if (this.formData.id && !this.isMiddle) {
                if (index === 3) {
                    this.$router.push({ name: 'Pmallocation', query: { schemeid: this.formData.id, editFlag: true } })
                } else {
                    this.activeStep = index
                    this.activeName = index.toString()
                }
            }
        },
        clearNoNum (obj) {
            obj = obj.replace(/[^\d.]/g, '')  // 清除数字和.以外的字符
            obj = obj.replace(/\.{2,}/g, '.') // 只保留第一个. 清除多余的
            obj = obj.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
            obj = obj.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3') // 只能输入两个小数
            obj = Number(obj)
        },
        // qiuyue 20201208 导入考核人
        updateGroup (res, chooseGroup) {
            if (res.statusCode === 200) {
                    if (!chooseGroup) {
                        this.ogroups.unshift(res.data)
                    }
                    this.chooseItem(res.data.id)
                    this.dialogVisible = false
                    this.$message({
                            type: 'success',
                            message: res.message
                    })
                    } else {
                    this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                    })
                    }
        },
        copyIdFun () {
            this.loading = true
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/copyScheme`, {
                type: 'Post',
                data: { 'id': this.oldSchemeId }
            }).then(res => {
                if (res.statusCode === 200) {
                    this.loading = false
                    this.programID = res.data
                    this.getData()
                } else {
                    this.loading = false
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            }).catch(err => {
                this.loading = false
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: err.message
                })
            })
        },
        onMoreSetFun () {
            this.$refs.moreSet.openFun()
        },
        /**
         * 以下为子组件所需要的公共方法
         */
        /**
         * @des 进度条 - 下一步事件
         * @param {String} id 当前方案id
         */
        addStepFun (id) {
            if (this.activeStep === 2 && this.formData.isresultaudited) {
                this.$router.push({ name: 'Pmallocation', query: { schemeid: this.formData.id, editFlag: true } })
            } else {
                this.programID = id
                this.getData()
            }
            this.activeStep++
            this.activeName = this.activeStep.toString()
        },
        /**
         * @des 子组件编辑方案名称时，父组件标题跟着修改
         * @param 修改后的方案名称
         */
        changeName (name) {
            this.formData.schemename = name
        }
    }
}
</script>
<style lang="less" scoped>
/* 2019-09-19 鲍帅朋添加 */
.prog-tag {
    width: 100%;
    display: flex;
    flex-direction: column;

    .prog-tag-top {
        display: flex;
        height: 35px;
        overflow: hidden;
        .tag-list {
            > span {
                margin-bottom: 5px;
                margin-right: 5px;

                color: #555;
                padding: 0 8px;
                font-size: 12px;
                line-height: 20px;
                border: 1px solid #bfbfbf;
                cursor: pointer;
                height: 22px;
                margin-right: 4px;
                border-radius: 3px;
                background: #fff;
            }
        }
        > div:first-child {
            flex: 1;
        }
        > div:nth-child(2),
        > div:nth-child(3) {
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 5px;
            cursor: pointer;
            transition: transform 0.3s, -webkit-transform 0.3s;
        }
        .add-tag {
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid hsla(214, 9%, 50%, 0.5);
            border-radius: 50%;
        }
    }
    .pull-icon-trans {
        transform: rotate(-180deg);
    }
    .show-more-prog-tag {
        height: auto !important;
    }
    .tag-add-input {
        margin-top: 5px;
    }
    .system-tag {
        background: #f3f3f3 !important;
    }
    .select-tag {
        color: #e14c46 !important;
        border: 1px solid #e14c46 !important;
    }
}
.el-tooltip__popper i {
    opacity: 0.45;
    color: #384d71;
    margin-right: 10px;
    font-size: 14px;
}
#program-edit.sn-container {
    /*background-color: transparent;*/
    /*padding: 0;*/
    /*<!--margin: -20px -20px 0 -30px;-->*/
}
#program-edit {
    /*overflow: hidden;*/
    .el-tooltip.wui-Warning {
        /*opacity: 0.45;*/
        /*color: #384D71;*/
    }
    #pane-0 {
        .program-content {
            max-width: 1500px;
            /*min-width: 1000px;*/
            .el-form-item {
                width: 400px;
                margin-right: 80px;
                /*margin-right: 40px;*/
                float: left;
            }
            .el-autocomplete,
            .el-select {
                /*width: 280px;*/
                width: 100%;
            }
        }
    }
    i.hover-icon {
        cursor: pointer;
        display: none;
        margin-right: 10px;
    }
    .pm-progress-bar {
        .progress-in {
            line-height: 2.2;
            background-color: #f2f2f2;
            text-align: center;
            position: relative;
            cursor: pointer;
            &::after {
                content: '';
                position: absolute;
                width: 0;
                height: 0;
                right: -30px;
                border-width: 15px;
                border-style: solid;
                border-color: transparent transparent transparent #f2f2f2;
            }
            &.active {
                background-color: #66cc99;
                color: #ffffff;
                &::after {
                    border-color: transparent transparent transparent #66cc99;
                }
            }
        }
    }
    .program-content {
        padding: 14px;
        /*margin-top:10px;*/
        /*border: 1px solid #f2f2f2;*/
    }
    .setting-item {
        border: 1px solid #dee0e3;
        box-shadow: 0 1px 1px 0 rgba(74, 81, 93, 0.1);
        border-radius: 3px;
        height: 240px;
        margin-bottom: 10px;
        padding: 18px 40px 26px 20px;
        /*cursor: pointer;*/
        position: relative;
        font-size: 12px;
        &:hover {
            box-shadow: 0 6px 8px 0 rgba(74, 81, 93, 0.1);
        }
        .title {
            font-size: 14px;
            color: #474d54;
            margin-bottom: 34px;
        }
        .icon-container {
            text-align: right;
            position: absolute;
            bottom: 26px;
            right: 20px;
            i {
                font-size: 30px;
                color: #7f838b;
            }
        }
    }
    #title-header {
        height: 26px;
        margin-bottom: 6px;
        h2 {
            margin: 0;
            margin-top: 3px;
            font-size: 16px;
            float: left;
        }
    }
}
.basic-back {
    background-color: #ffffff;
    border-radius: 3px;
    /*padding: 16px 14px;*/
}
.pro-operation {
    margin-top: 12px;
    background-color: #ffffff;
    text-align: right;
    /*padding-bottom: 24px;*/
}

@media all and (max-width: 1000px) {
    #program-edit #pane-0 {
        .program-content {
            /*width: 600px;*/
        }
    }
}
</style>
<style lang="less">
#program-edit {
    .header-wrap.header-fixed + div {
        padding-top: 62px;
    }
    .header-wrap {
        z-index: 1001;
        .operation {
            width: 50%;
        }
        .header-title {
            width: calc(~'50% - 30px');
            white-space: nowrap;
            overflow: hidden;
            div {
                text-overflow: ellipsis;
                width: 100%;
                overflow: hidden;
            }
        }
    }
    .el-table__empty-block {
        height: 100%;
    }
    .eltab .el-tabs__header {
        display: none;
        .el-tabs__item.is-disabled {
            color: #b0b2b7;
            &.is-active {
                color: #474d54;
            }
        }
    }
    .mytabheader {
        z-index: 1000;
        position: absolute;
        opacity: 0;
        /*left:0;*/
        /*top:0;*/
        .el-tabs__item {
            &#tab-first {
                padding-left: 0;
            }
            &:last-child {
                padding-right: 0;
            }
            &.is-active {
                border-bottom: 2px solid #e14c46;
            }
        }
    }
    .fade-enter-active,
    .fade-leave-active {
        transition: all 0.5s;
    }
    .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
        /*opacity: 0;*/
        /*margin-left:0;*/
        transform: translateX(10px);
    }
    .el-tabs__active-bar {
        /*width:56px;*/
    }
    .table-apprasier {
        .el-form-item {
            margin-bottom: 0;
        }
        .el-table__empty-block {
            /*height:200px;*/
            > span {
                color: #007ace;
                cursor: pointer;
            }
        }
    }
    .el-tooltip.formtip {
        position: absolute;
        right: -30px;
        top: 0;
    }
    .obj-dialog {
        .el-autocomplete {
            width: 100%;
            cursor: pointer;
            /* opacity: 0;
         .el-input__icon{
           !*padding-left:50%;*!
           !*padding-right: 50%;*!
           padding-bottom:14%;
           margin-right:-50%!important;
           font-size:200px!important;
         }*/
        }
    }
    .el-table--enable-row-hover .el-table__body tr:hover > td {
        i.hover-icon {
            display: inline-block;
        }
    }
    .el-input-number {
        span {
            display: none;
        }
    }
    .el-form-item.inline {
        display: inline-block;
        vertical-align: top;
        margin-right: 24px;
        .el-form-item__content {
            display: inline-block;
            vertical-align: top;
        }
        .el-form-item__label {
            display: inline-block;
        }
    }
    .el-table {
        /*.el-input-number.is-required{*/
        /*.el-input::before{*/
        /*content: '*';*/
        /*color:    #D12922;*/
        /*position: absolute;*/
        /*left:15px;*/
        /*height:100%;*/
        /*}*/
        /*}*/
        .el-input.is-required,
        .el-select.is-required {
            .el-input__inner {
                &::-webkit-input-placeholder {
                    /* WebKit browsers */
                    color: #d12922;
                }
                &:-moz-placeholder {
                    /* Mozilla Firefox 4 to 18 */
                    color: #d12922;
                }
                &::-moz-placeholder {
                    /* Mozilla Firefox 19+ */
                    color: #d12922;
                }
                &:-ms-input-placeholder {
                    /* Internet Explorer 10+ */
                    color: #d12922;
                }
            }
            /*.el-input::before{*/
            /*content: '*';*/
            /*color:#D12922;*/
            /*position: absolute;*/
            /*left:14px;*/
            /*}*/
        }
    }
    #pane-0 {
        .el-checkbox {
            margin-right: 30px;
            margin-left: 0;
        }
    }
    #pane-1 {
        .el-form-item__content {
            line-height: inherit;
        }
    }
    .obj-dialog {
        .el-dialog {
            min-width: 700px;
        }
    }
}
.en_US {
    #program-edit {
        .setting-item {
            height: 400px;
        }
    }
}
@media only screen and (min-width: 992px) {
    .en_US {
        #program-edit {
            .setting-item {
                height: 400px;
            }
        }
    }
}

@media only screen and (min-width: 992px) and (max-width: 1200px - 1) {
    .en_US {
        #program-edit {
            .setting-item {
                height: 300px;
            }
        }
    }
}

@media only screen and (min-width: 1199px) {
    .en_US {
        #program-edit {
            .setting-item {
                height: 300px;
            }
        }
    }
}
</style>
