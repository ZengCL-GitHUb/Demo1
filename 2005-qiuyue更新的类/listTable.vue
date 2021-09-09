<template>
    <div id="pm-object">
        <el-row :gutter="10"
                id="">
            <el-col :span="5">
                <div id="object-list"
                     ref="content"
                     class="content"
                     :style="{height: tableHeight + 38 + 'px'}">
                    <div class="list-header">
                        <!--考核对象组-->
                        <span class="iconfont wui-Add_bold"
                              @click="addGroup"
                              v-show="!editFlag"></span>
                        <span class="iconfont wui-Dustbin_bold"
                              @click="del"
                              v-show="!editFlag&listData.length>0"></span>
                        <span class="iconfont wui-Record_bold"
                              @click="change()"
                              v-show="!editFlag&listData.length>0"></span>
                    </div>
                    <el-scrollbar style="height: calc(100% - 38px)">
                        <ul>
                            <li v-for="(item,index) in listData"
                                :key="item.id"
                                :class="[activeItem === item.id ? 'active' : '']"
                                @click.stop="chooseItem(item.id, item.groupname, index)">
                                <el-tooltip effect="dark"
                                            :content="item.groupname"
                                            placement="bottom-start"
                                            :visible-arrow="false"
                                            :enterable="false">
                                    <span v-show="editId!== item.id"
                                          class="group-name">{{item.groupname}}</span>
                                </el-tooltip>
                                <!--<div v-show="editId!== item.id">-->
                                <!--<i class="lifont iconfont wui-Dustbin_bold" v-show="!editFlag" @click.stop="del(item.id, index)" ></i>-->
                                <!--<i class="lifont iconfont wui-Record_bold" v-show="!editFlag" @click.stop="change(item.id)"></i>-->
                                <!--</div>-->
                                <el-input v-show="editId=== item.id"
                                          v-model="activeGroupname"></el-input>
                                <div v-show="editId=== item.id">
                                    <i class="iconfont wui-close"
                                       v-show="!editFlag"
                                       @click.stop="editId = '',activeGroupname = item.groupname"></i>
                                    <i class="iconfont wui-confirm"
                                       v-show="!editFlag"
                                       @click.stop="edit(item,index)"></i>
                                </div>
                            </li>
                        </ul>

                    </el-scrollbar>
                    <!--<div  @click="addGroup" class="add-group" v-show="!editFlag"> + &lt;!&ndash;新建对象组&ndash;&gt;{{_sn('pm.i18n365')}}</div>-->
                </div>
            </el-col>
            <el-col :span="19">
                <el-row style="padding-bottom:6px">
                   

                    <!--{{typeof name}} {{name}}-->
                    <el-button type="info"
                               @click="toAllocation"
                               v-show="listData.length > 0 && editFlag && schememode !== 20">
                        <!--结果审核流程设置-->{{_sn('pm.i18n229')}}</el-button>
                    <div class="addicon">
                        <!--<p v-show="listData.length > 0 && !editFlag">+ &lt;!&ndash;添加组员&ndash;&gt;{{_sn('pm.i18n125')}}</p>-->
                        <Ref v-if="refParams.applyobj!==2&&listData.length > 0 && !editFlag"
                             :model="model"
                             :field="field"
                             refCode="staff_tree_grid_ref"
                             :refParam="{ refTitle: _sn('common.i18n29'),multi: true,diaWidth:'70%',customTableHeaders: [
                     {prop: 'refname', label: _sn('common.i18n4')},
                     {prop: 'refcode', label: _sn('common.i18n11')},
                     {prop: 'deptName', label: _sn('common.i18n20')}
                   ]}"
                             :clientParam="{serviceCode: 'HRPM0010', depttype: -1}"
                             :labelField="labelField"
                             :placeholder="_sn('pm.i18n363')"
                             :customize="true"
                             @afterReference="afterReference"
                             refTo="ref-wrap">
                            <!--所有人员-->
                            <el-button type="info">{{_sn('pm.i18n125')}}</el-button>
                        </Ref>
                        <Ref v-else-if="listData.length > 0 && !editFlag"
                             :model="modelOrg"
                             field="refpk"
                             refCode="org_ref"
                             :refParam="{ refTitle:_sn('pm.i18n1378'),multi: true, diaWidth:'70%'}"
                             :clientParam="{serviceCode: 'HRPM0010', depttype: -1}"
                             labelField="refname"
                             :placeholder="_sn('pm.i18n363')"
                             :customize="true"
                             @afterReference="afterRefOrgs"
                             refTo="ref-wrap">
                            <!--选择组织-->
                            <!--所有人员-->
                            <el-button type="primary">{{_sn('common.i18n8')}}
                                <!--新增-->
                            </el-button>
                        </Ref>
                        <Ref :model="gauge"
                             field="refpk"
                             refCode="pm_gauge_grid_ref"
                             :refParam="{ refTitle: _sn('pm.i18n1337'),
                 refTips: _sn('pm.i18n1288'),
                  customTableHeaders:[
                     {prop: 'refname',label: _sn('pm.i18n1338')},
                     {prop: 'remark',label: _sn('pm.i18n636')}
                   ]}"
                             :clientParam="refParams"
                             labelField="refname"
                             :customize="true"
                             @afterReference="saveGauge"
                             refTo="ref-wrap"
                             v-if="!editFlag"
                             :disabled="!selectData.length">
                            <!--公共考核量表名称 说明-->
                            <el-button type="info"
                                       :disabled="!selectData.length">
                                <!--设置量表-->{{_sn('pm.i18n1337')}}</el-button>
                        </Ref>
                        <!--:clientParam="refParams"-->
                        <!--修改考核流程-->
                        <el-button type="info"
                                   @click="showFlow"
                                   v-show="!editFlag"
                                   :disabled="!selectData.length">{{_sn('pm.i18n1285')}}</el-button>
                        <el-button type="info"
                                   @click="exportFun"
                                   :disabled="!listData.length">{{_sn('pm.i18n1340')}}</el-button>
                        <el-button type="info"
                                   @click="delItems()"
                                   v-show="!editFlag"
                                   :disabled="!selectData.length">{{_sn('common.i18n9')}}</el-button>

                         <!-- qiuyue on 20201201 -->
                       <el-button type="info" 
                                  @click="exportExcel"
                                   v-show="!editFlag"
                       >导出模板</el-button>
                       <div style="display:inline;"><input id="uploadfile" type="file" multiple="multiple" v-on:change="importExcel"  style="top:0;height:29px;width:87px; right:0; opacity:0;position: absolute;" /></div>
                      <el-button  type="info"  v-show="!editFlag">导入考核人</el-button>
                       
                       <!-- qiuyue ---------end -->
                    </div>
                </el-row>
                <el-table :data="data"
                          class="border-table"
                          border
                          element-loading-spinner="iconfont wui-Loading"
                          v-loading.fullscreen="loading"
                          @selection-change="handleSelectionChange"
                          :max-height="tableHeight"
                          :height="tableHeight"
                          style="width:100%">
                    <el-table-column type="selection"
                                     width="55"
                                     fixed>
                    </el-table-column>
                    <el-table-column type="index"
                                     :label="_sn('common.i18n16')"
                                     width="50"
                                     fixed>
                    </el-table-column>
                    <el-table-column v-for="item in headDataList"
                                     :key="item.value"
                                     :prop="item.value"
                                     :label="item.name"
                                     :fixed="item.fixed"
                                     :min-width="item.width"
                                     show-overflow-tooltip
                                     :sortable="item.type!=='search'||item.type==='search'&&!item.searching">
                        <template slot="header">
                            <span v-if="item.type==='search' || item.type==='ref'">
                                <TableSearch :headerType="item.type"
                                             :formatData="{label: 'name',prop:item.type==='search'?'svalue':'ivalue',refCode:'org_ref'}"
                                             :data="item"
                                             :model="group"
                                             :refParam="{refTitle: _sn('pm.i18n247'),multi:true,diaWidth: '70%'}"
                                             :clientParam="{serviceCode: 'HRPM0010'}"
                                             @search="searchByHeader"
                                             @changeSearchLists="changeSearchLists(arguments,item)"
                                             @toggleSearch="toggleSearching(item)">
                                </TableSearch>
                            </span>
                            <!-- <span v-else-if="item.type==='ref'" class="header-expand">
                <span>{{item.name}}</span>
                <Ref :model="group"
                             class="header-right"
                             field="refpk"
                             refCode="org_ref"
                             :refParam="{refTitle: _sn('pm.i18n247'),multi:true,diaWidth: '70%'}"
                             :clientParam="{serviceCode: 'HRPM0010'}"
                             labelField="refname"
                             placeholder=""
                             refTo="ref-wrap"
                             :customize="true"
                             @afterReference="filterByDept">
                            <i class="iconfont wui-shaixuan" v-if="deptIds.length"></i>
                            <i class="iconfont wui-Tabulation_bold" v-else></i>
                </Ref>
                选择部门 , orgtype:2
              </span>
              </template>-->
                            <span v-else>{{item.name}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column width="180"
                                     show-overflow-tooltip
                                     :label="_sn('pm.i18n1287')"
                                     prop="name"
                                     v-if="refParams.isallowfillup">
                        <template slot-scope="scope">
                            <span v-for="(staff,i) in scope.row.approverList"
                                  :key="i">
                                <i class="iconfont wui-dotted-right"
                                   v-show="i!==0&&scope.row.approverList[i-1]"></i>
                                {{staff? staff.name: ''}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column width="180"
                                     show-overflow-tooltip
                                     label="指标下达人"
                                     v-if="indisource === 2"
                                     prop="name">
                        <template slot-scope="scope">
                            <span v-for="(staff,i) in scope.row.approverList"
                                  :key="i">
                                <i class="iconfont wui-dotted-right"
                                   v-show="i!==0&&scope.row.approverList[i-1]"></i>
                                {{staff? staff.name: ''}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column min-width="180"
                                     show-overflow-tooltip
                                     :label="_sn('common.i18n31')"
                                     prop="name">
                        <template slot-scope="scope">
                            <span v-for="(turn,i) in scope.row.appraiserList"
                                  :key="i">
                                <i class="iconfont wui-dotted-right"
                                   v-show="i!==0&&scope.row.appraiserList[i-1]&&(scope.row.appraiserList[i-1].length>1||scope.row.appraiserList[i-1][0])&&turn.length"></i>
                                <span v-for="(staff, ki) in turn"
                                      :key="ki">{{staff? staff.name: ''}}{{ki==turn.length-1?'': '、'}}</span>
                            </span>
                        </template>
                    </el-table-column>
                    <!--<el-table-column width="180" align="right">-->
                    <!--<template slot-scope="scope">-->
                    <!--<i class="iconfont wui-Dustbin_bold hover-icon" v-show="!editFlag"-->
                    <!--@click="delItem(scope.$index, scope.row)"></i>-->
                    <!--</template>-->
                    <!--</el-table-column>-->
                    <span slot="empty">
                        <sn-empty></sn-empty>
                    </span>
                </el-table>
            </el-col>
        </el-row>
        <el-dialog :title="_sn('pm.i18n1285')"
                   :visible.sync="dialogVisible"
                   :before-close="closeFlow"
                   class="flow-dialog"
                   width="70%">
            <el-menu :default-active="activeFlow"
                     class="el-menu-demo"
                     mode="horizontal"
                     @select="handleSelect">
                <!--v-loading.fullscreen.lock="fullscreenLoading" element-loading-spinner="iconfont wui-Loading"-->
                <!--审核人设置-->
                <el-menu-item index="objectApprover"
                              v-show="refParams.isallowfillup">
                    {{_sn('common.i18n69')}}</el-menu-item>
                <!--指标下达人-->
                <el-menu-item index="indisource"
                              v-show="indisource === 2">
                    {{'指标下达人'}}</el-menu-item>
                <!-- 考核人设置 -->
                <el-menu-item index="objectAppraiser">{{_sn('pm.i18n440')}}</el-menu-item>
            </el-menu>
            <flowTable :tableData="flowTableData['objectApprover']"
                       :headData="flowHead[activeFlow]"
                       key="objectApprover"
                       :indisource="indisource"
                       v-show="(activeFlow==='objectApprover'&&refParams.isallowfillup)"
                       activeFlow="objectApprover"
                       ref="objectApprover"
                       :diaTableLoading="diaTableLoading">
            </flowTable>
            <flowTable :tableData="flowTableData['indisource']"
                       :headData="flowHead[activeFlow]"
                       key="indisource"
                       :indisource="indisource"
                       v-show="indisource === 2 && activeFlow !=='objectAppraiser'"
                       activeFlow="indisource"
                       ref="indisource"
                       :diaTableLoading="diaTableLoading">
            </flowTable>
            <flowTable :tableData="flowTableData['objectAppraiser']"
                       key="objectAppraiser"
                       :indisource="indisource"
                       :headData="flowHead[activeFlow]"
                       v-show="activeFlow==='objectAppraiser'"
                       activeFlow="objectAppraiser"
                       ref="objectAppraiser"
                       :diaTableLoading="diaTableLoading">
            </flowTable>
            <!--取消-->
            <div slot="footer"
                 class="dialog-footer">
                <el-button type="text"
                           @click="dialogVisible = false">
                    <!--取消-->{{_sn('common.cancel')}}</el-button>
                <el-button type="primary"
                           @click="saveFlow"
                           :loading="saving">
                    <!--确认-->{{_sn('pm.i18n754')}}</el-button>
            </div>
        </el-dialog>
        <!-- 导出 -->
        <el-dialog :title="_sn('pm.i18n1340')"
                   :visible.sync="exportVisible"
                   :before-close="closeExport"
                   @close="test"
                   class="flow-dialog"
                   width="70%">
            <!--取消-->
            <el-table class="border-table"
                      border
                      :data="listData"
                      ref="exportTable"
                      @selection-change="handleSelectFun">
                <el-table-column type="selection"
                                 width="55">
                </el-table-column>
                <el-table-column property="groupname"
                                 :label="_sn('pm.i18n1341')">
                    <!--组名称-->
                </el-table-column>
            </el-table>
            <div slot="footer"
                 class="dialog-footer">
                <el-button type="text"
                           @click="exportVisible = false">
                    <!--取消-->{{_sn('common.cancel')}}</el-button>
                <el-button type="primary"
                           @click="exportDialogFun"
                           :loading="saving">
                    <!--确认-->{{_sn('pm.i18n754')}}</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import { fetchData } from '@/utils/util.js'
import Ref from '@/components/controls/reference'
import flowTable from './flowTable'
import gauge from '@/pages/pm/mixins/gauge.js'
import TableSearch from '../common/tablesearch'
import XLSX from 'xlsx'

const { performance } = window.ServiceConfigs
export default {
    name: 'listTable',
    mixins: [gauge],
    components: {
        Ref, flowTable, TableSearch
    },
    props: {
        listData: [Array],
        tableData: [Array],
        editFlag: false,
        name: this.field,
        schemeid: '',
        refParams: {
            type: Object,
            default: {}
        },
        tableHeight: Number,
        indisource: Number,
        schememode: Number
    },
    data: function () {
        return {
            exportVisible: false,
            secExpData: [], // 导出选择文件
            activeItem: '',
            field: 'staffid',
            labelField: 'refname',
            model: {
                staffid: '',
                refname: ''
            },
            modelOrg: {
                refpk: '',
                refname: ''
            },
            group: {
                refpk: '',
                refname: ''
            },
            data: [],
            groupFlag: true,
            editId: '',
            loading: false,
            activeGroupname: '',
            selectData: [],
            dialogVisible: false,
            flowTableData: {},
            activeFlow: '',
            flowHead: {
                objectApprover: [
                    {
                        name: this._sn('common.i18n49'),
                        value: '',
                        required: true,
                        fixed: true,
                        type: 'text'
                    }, // <!--轮次-->
                    {
                        name: this._sn('pm.i18n123'),
                        value: 'staffid',
                        type: 'ref'
                    }, // <!--审核人-->
                    {
                        name: this._sn('pm.i18n1286'),
                        value: 'ostaff.corpname',
                        type: 'ref'
                    }, // <!--任职组织-->
                    {
                        name: this._sn('common.i18n64'),
                        value: 'ostaff.deptname',
                        type: 'text'
                    }, // <!--部门-->
                    {
                        name: this._sn('common.i18n19'),
                        value: '',
                        type: 'action'
                    } // <!--操作-->
                ],
                indisource: [
                    {
                        name: '指标下达人',
                        value: 'staffid',
                        type: 'ref'
                    }, // 指标下达人
                    {
                        name: this._sn('pm.i18n1286'),
                        value: 'ostaff.corpname',
                        type: 'ref'
                    }, // <!--任职组织-->
                    {
                        name: this._sn('common.i18n64'),
                        value: 'ostaff.deptname',
                        type: 'text'
                    } // <!--部门-->
                ],
                objectAppraiser: [
                    {
                        name: this._sn('common.i18n49'),
                        value: '',
                        required: true,
                        fixed: true,
                        type: 'text'
                    }, // <!--轮次-->
                    {
                        name: this._sn('common.i18n70'),
                        value: 'dimid',
                        label: 'dimname',
                        type: 'select',
                        option: 'made'
                    }, // <!--考核人类别-->
                    {
                        name: this._sn('pm.i18n81'),
                        value: 'weight',
                        type: 'input'
                    }, // <!--考核权重-->
                    {
                        name: this._sn('pm.i18n106'),
                        value: '',
                        type: 'ref'
                    }, // <!--指定考核人-->
                    {
                        name: this._sn('common.i18n19'),
                        value: '',
                        type: 'action'
                    } // <!--操作-->
                ]
            },
            // tableHeight: 450,
            searchName: '',
            basicData: [],
            headerSearch: false,
            diaTableLoading: false,
            saveFlowData: {},
            deptIds: [],
            saving: false,
            headData: [
                {
                    name: this._sn('pm.i18n262'),
                    value: 'staff.code',
                    svalue: 'code',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '130'
                }, // <!--人员编码-->
                {
                    name: this._sn('common.i18n24'),
                    value: 'staff.name',
                    svalue: 'name',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '110'
                }, // <!--姓名-->
                {
                    name: this._sn('pm.i18n1286'),
                    value: 'staff.corpname',
                    svalue: 'corpname',
                    width: '120'
                }, // <!--任职组织-->
                {
                    name: this._sn('common.i18n20'),
                    value: 'staff.deptname',
                    svalue: 'deptname',
                    required: true,
                    type: 'ref',
                    width: '130',
                    ivalue: 'deptid'
                }, // <!--部门-->
                {
                    name: this._sn('common.i18n73'), //  <!--职位-->
                    value: 'staff.postname',
                    svalue: 'postname'
                },
                {
                    name: this._sn('common.i18n64'),
                    value: 'staff.jobname'
                }, // <!--职务-->
                {
                    name: '职级',
                    value: 'staff.jobgradename',
                    svalue: 'jobgradename',
                    required: true,
                    type: 'search',
                    width: '110'
                }, // <!--职级-->
                {
                    name: this._sn('common.i18n28'),
                    value: 'staff.psnclname',
                    svalue: 'psnclname',
                    width: '110'
                } // <!--员工类别-->
            ],
            headDataGauge: [
                {
                    name: this._sn('pm.i18n262'),
                    value: 'staff.code',
                    svalue: 'code',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '130'
                }, // <!--人员编码-->
                {
                    name: this._sn('common.i18n24'),
                    value: 'staff.name',
                    svalue: 'name',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '110'
                }, // <!--姓名-->
                {
                    name: this._sn('pm.i18n1286'),
                    value: 'staff.corpname',
                    svalue: 'corpname',
                    width: '120'
                }, // <!--任职组织-->
                {
                    name: this._sn('common.i18n20'),
                    value: 'staff.deptname',
                    svalue: 'deptname',
                    required: true,
                    type: 'ref',
                    width: '130',
                    ivalue: 'deptid'
                }, // <!--部门-->
                {
                    name: this._sn('common.i18n73'), //  <!--职位-->
                    value: 'staff.postname',
                    svalue: 'postname'
                },
                {
                    name: this._sn('common.i18n64'),
                    value: 'staff.jobname'
                }, // <!--职务-->
                {
                    name: '职级',
                    value: 'staff.jobgradename',
                    svalue: 'jobgradename',
                    required: true,
                    type: 'search',
                    width: '110'
                }, // <!--职级-->
                {
                    name: this._sn('common.i18n28'),
                    value: 'staff.psnclname',
                    svalue: 'psnclname',
                    width: '110'
                }, // <!--员工类别-->
                {
                    name: this._sn('pm.i18n1343'),
                    value: 'gaugename',
                    svalue: 'gaugename',
                    width: '110'
                } // <!--考核量表-->
            ],
            headDataOrg: [
                {
                    name: '组织名称',
                    value: 'staff.corpname',
                    svalue: 'corpname',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '130'
                }, // <!--组织编码-->
                {
                    name: '部门名称',
                    value: 'staff.deptname',
                    svalue: 'deptname',
                    required: true,
                    fixed: true,
                    type: 'search',
                    width: '110'
                }, // <!--组织名称-->
                {
                    name: '负责人',
                    value: 'staff.name',
                    svalue: 'name',
                    width: '120'
                }, // <!--组织负责人-->
                {
                    name: '考核量表',
                    value: 'gaugename',
                    svalue: 'gaugename',
                    width: '110'
                } // <!--考核量表-->
            ],
            dialogMoreVisible: false,
            gauge: {
                refpk: '',
                refname: ''
            },
            lists: {},
            searchLists: {}
            // headDataList: []
        }
    },
    watch: {
        listData (newVal, oldVal) {
            // 左树对象组数据变化 包含初始赋值和删除增加（增加到最前） 默认选中第一个
            if (newVal.length !== 0) {
                this.chooseItem(newVal[0].id, newVal[0].groupname, 0)
            }
        },
        tableData: {
            deep: true,
            handler (val, old) {
                let arr = val
                if (this.refParams.applyobj !== 2) {
                    arr.forEach(item => {
                        this.headData.forEach(i => {
                            if (!item.staff[i.svalue]) {
                                item.staff[i.svalue] = ''
                            }
                        })
                        // if (!item.staff.psnclname) {
                        //   item.staff.psnclname = ''
                        // }
                    })
                }
                this.data = arr
                this.basicData = arr
            }
        }
    },
    computed: {
        headDataList () {
            // debugger
            // this.$nextTick(() => {
            //     return (this.tFlag || this.yFlag) ? this.refParams.applyobj === 2 ? this.headDataOrg : this.headDataGauge : this.headData
            // })
            if (this.refParams.applyobj && this.refParams.applyobj === 2) {
                // debugger
                return this.headDataOrg
            } else if (this.refParams.applyobj) {
                // debugger
                return this.headDataGauge
            }
            // else if (this.refParams.applyobj) {
            //   // debugger
            //   return this.headData
            // }
            // })
        },
        defaultFlow () {
            return this.indisource === 2 ? 'indisource' : this.refParams.isallowfillup ? 'objectApprover' : 'objectAppraiser'
        }
    },
    created: function () {
        // this.$nextTick(() => {
        //   if (this.tFlag || this.yFlag) {
        //     debugger
        //     this.headDataList = this.refParams.applyobj === 2 ? this.headDataOrg : this.headDataGauge
        //   } else {
        //     this.headDataList = this.headData
        //   }
        // })
        this.getData()
    },
    mounted: function () {
    },
    methods: {
       
        showInput (e) {
            this.headerSearch = true
            this.$nextTick(() => {
                this.$refs['name-input'][1].$refs.input.focus()
            })
        },
        sortMethod (a, b) {
            if (a.staff.corpname < b.staff.corpname) {
                return -1
            } else if (a.staff.corpname > b.staff.corpname) {
                return 1
            } else {
                return 0
            }
        },
        formatter (
            row, column) {
            //   return row[column.property]
        },
        change (id = '') {
            // this.editId = id
            // debugger
            this.editId = id || this.activeItem
        },
        afterReference (data) {
            this.$emit('afterObject', data, this.activeItem)
        },
        getData () {
            this.data = this.tableData
            this.basicData = this.tableData
        },
        addGroup () {
            this.$emit('addGroup')
        },
        chooseItem (id, name, index) {
            // debugger
            if (this.activeItem !== id) {
                this.editId = ''
            }
            this.deptIds = []
            this.group = {
                refpk: '',
                refname: ''
            }
            this.searchName = ''
            this.headerSearch = false
            this.$emit('choose-item', id)
            this.activeItem = id
            this.activeGroupname = name
            this.activeIndex = index
        },
        edit (item, index) {
            let data = item
            data.groupname = this.activeGroupname
            fetchData(`${window.hrPath}${performance.core}/pm/system/saveEvaGroup`, {                type: 'Post',
                data
            }).then(res => {
                this.listData.forEach((item, i) => {
                    if (index === i) {
                        this.editId = ''
                    }
                })
                this.$message({
                    type: 'success',
                    message: res.message
                })
            })
        },
        del (id, index) {
            // let data = {id: id}
            let data = { id: this.activeItem }
            let o = this
            // <!--确认删除？-->
            this.$confirm(this._sn('pm.i18n366'), {
                confirmButtonClass: 'el-button--warning',
                type: 'warning'
            })
                .then(_ => {
                    fetchData(`${window.hrPath}${performance.pbc}/pm/systempbc/deleteEvaGroup`, {                        type: 'Post',
                        data
                    }).then(res => {
                        if (res.statusCode === 200) {
                            this.listData.forEach((item, i) => {
                                // if (index === i) {
                                if (o.activeIndex === i) {
                                    o.listData.splice(i, 1)
                                }
                            })
                            this.$message({
                                type: 'success',
                                message: res.message
                            })
                            // this.getData()
                            if (this.listData[0]) {
                                this.chooseItem(this.listData[0].id, this.listData[0].groupname)
                            } else {
                                this.data = []
                                this.basicData = []
                            }
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    }).catch(err => {
                        this.$message({
                            type: 'error',
                            duration: 5000,
                            message: err.message
                        })
                    })
                }).catch(_ => {
                    // this.listData.forEach(item => {
                    // this.$set(item, 'groupFlag', true)
                    // })
                })
        },
        delItems () {
            let param = this.selectData.map(item => {
                return item.id
            })
            this.$confirm(this._sn('pm.i18n366'), {
                confirmButtonClass: 'el-button--warning',
                type: 'warning'
            }).then(_ => {
                this.loading = true
                fetchData(`${window.hrPath}${performance.pbc}/pm/systempbc/deleteEvaObject`, {
                    type: 'Post',
                    data: param
                }).then(res => {
                    this.loading = false
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        })
                        this.chooseItem(this.activeItem, this.activeGroupname, this.activeIndex)
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                    // this.data.splice(index, 1)
                }).catch(() => {
                    this.loading = false
                })
            }).catch(() => {
                this.loading = false
            })
        },
        showFlow () {
            this.loading = true
            this.getIndiApprover()
        },
        showGauge () {
            this.dialogMoreVisible = true
        },
        getIndiApprover () {
            let param = this.selectData.map(item => {
                return item.id
            })
            fetchData(`${window.hrPath}${performance.core}/pm/system/queryObjectAppraiser`, {
                type: 'Post',
                data: param
            }).then(res => {
                this.loading = false
                if (res.statusCode === 200) {
                    let flowTableData = res.data
                    if (flowTableData.objectApprover.length < 1) {
                        flowTableData.objectApprover = [
                            {
                                'staffid': '',
                                'staffname': '',
                                'ostaff': {}
                            }
                        ]
                    } else {
                        flowTableData.objectApprover.forEach(item => {
                            if (!item || !item.ostaff) {
                                item.ostaff = {}
                            }
                        })
                    }
                    flowTableData.indisource = flowTableData.objectApprover
                    if (res.data.objectAppraiser.length < 1) {
                        flowTableData.objectAppraiser = [
                            {
                                'dimid': '',
                                'weight': null,
                                'ostaffList': [
                                    {
                                        'id': '',
                                        'name': ''
                                    }
                                ],
                                'ostaff': {
                                }
                            }
                        ]
                    } else {
                        flowTableData.objectAppraiser.forEach(item => {
                            // item.weight.toString()
                            item.weight = typeof item.weight !== 'undefined' ? (item.weight * 100).toFixed(0) : ''
                            let arr = item.ostaffList.map(i => {
                                return i.id
                            })
                            let arrname = item.ostaffList.map(i => {
                                return i.name
                            })
                            item.ostaff = {
                                id: arr.join(','),
                                name: arrname.join(',')
                            }
                        })
                    }
                    this.loading = false
                    this.flowTableData = flowTableData
                    this.saveFlowData = flowTableData
                    this.activeFlow = this.defaultFlow // 'objectApprover'
                    this.dialogVisible = true
                } else {
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
        delItem (index, rows) {
            // let pdata = rows
            // let data = []
            let data = [rows.id]
            // pdata.forEach((item, i) => {
            //   if (index !== i) {
            //     data.push(item.refpk)
            //   }
            // })
            fetchData(`${window.hrPath}${performance.pbc}/pm/systempbc/deleteEvaObject`, {
                type: 'Post',
                data
            }).then(res => {
                if (res.statusCode === 200) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    })
                    this.data.splice(index, 1)
                    this.basicData.splice(index, 1)
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            })
        },
        toAllocation () {
            this.$emit('toAllocation')
        },
        handleSelectionChange (arr) {
            this.selectData = arr
            // this.indexLists = this.selectData.map(item => {
            //   return item.id
            // })
            // 赋值给model参照回显
            let staff = []
            let staffname = []
            let org = []
            arr.forEach(item => {
                staff.push(item.staffid)
                staffname.push(item.staff.name)
                org.push(item.orgid)
            })
            // debugger
            this.model = {
                staffid: staff.join(','),
                staffname: staffname.join(',')
            }
            this.modelOrg = {
                refpk: org.join(','),
                refname: ''
            }
            // debugger
            // this.gauge = {
            //  refpk: staff.join(','),
            //   refname: staffname.join(',')
            // }
        },
        handleSelect (index) {
            this.diaTableLoading = true
            // this[this.activeFlow] = this.$refs.flow
            //   this.saveFlowData[this.activeFlow] = this.$refs.flowTable.oevaAppraiser
            //   this.flowTableData[this.activeFlow] = this.$refs.flowTable.oevaAppraiser

            this.activeFlow = index
            this.$nextTick(() => {
                this.diaTableLoading = false
            })
        },
        closeFlow (done) {
            // this.selectData = []
            this.activeFlow = this.defaultFlow
            this.saving = false
            done()
        },
        saveFlow () {
            this.saving = true
            let approver = JSON.parse(JSON.stringify(this.$refs.objectApprover.oevaAppraiser))
            let appraiser = JSON.parse(JSON.stringify(this.$refs.objectAppraiser.oevaAppraiser))
            let indisource = JSON.parse(JSON.stringify(this.$refs.indisource.oevaAppraiser))
            // let idata = JSON.parse(JSON.stringify(this.flowTableData))
            // this.diaTableLoading = true
            let idata = JSON.parse(JSON.stringify(this.flowTableData))
            // this.diaTableLoading = true
            console.log(['approver', approver, 'appraiser', appraiser])
            console.log('flowTableData', idata)
            let objectApprover = approver.map(i => {
                if (i.ostaff) {
                    return i.ostaff.id
                } else {
                }
            })
            let indisourceMap = indisource.map(i => {
                if (i.ostaff) {
                    return i.ostaff.id
                } else {
                }
            })
            let sum = appraiser.reduce((total, sum) => {
                return total + parseInt(sum.weight)
            }, 0)
            // 权重和100检测
            if (sum !== 100) {
                this.saving = false
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: this._sn('pm.i18n1379') + '100%' // '考核人权重之和不等于100%' this._sn('ssc.i18n339')
                })
            } else {
                let objectAppraiser = appraiser.map(item => {
                    let turn = item
                    turn.weight = item.weight / 100
                    turn.staffids = item.ostaff ? item.ostaff.id && item.ostaff.id.split(',') : ''
                    return turn
                })
                let param = {
                    schemeid: this.schemeid,
                    evaobject_ids: this.selectData.map(item => {
                        return item.id
                    }),
                    objectApprover: this.indisource === 2 ? indisourceMap : objectApprover,
                    objectAppraiser: objectAppraiser
                }
                fetchData(`${window.hrPath}${performance.core}/pm/system/saveObjectAppraiser`, {
                    type: 'Post',
                    data: param
                }).then(res => {
                    this.saving = false
                    this.diaTableLoading = false
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        })
                        this.chooseItem(this.activeItem, this.activeGroupname, this.activeIndex)
                        this.dialogVisible = false
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                }).catch(err => {
                    this.saving = false
                    this.diaTableLoading = false
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
            }
        },
        searchByHeader (flag, val) {
            let data = this.basicData
            let arr = data
            let that = this
            if (typeof val === 'object') {
                // 参照筛选（多选-多条件筛选）
                for (let i in that.searchLists) {
                    arr = arr.filter(item => item.staff[i].indexOf(that.searchLists[i]) > -1)
                }
                this.data = val.length ? arr.filter(item => val.indexOf(item.staff[flag]) > -1) : arr
            } else {
                this.changeSearchLists(arguments, val).then(() => {
                    // input检索字段 先添加当前搜索字段到条件
                    for (let i in that.searchLists) {
                        arr = arr.filter(item => item.staff[i].indexOf(that.searchLists[i]) > -1)
                    }
                    this.data = arr
                    this.searchFlag = flag
                })
            }
        },
        changeSearchLists (value, i) {
            // flag, val
            // 前端过滤暂时不需要循环判断所有搜索项，直接每次偶所示
            let o = this
            return new Promise((resolve, reject) => {
                if (value[1]) {
                    o.searchLists[value[0]] = value[1]
                } else {
                    o.searchLists[value[0]] = ''
                }
                resolve(true)
            })
        },
        toggleSearching (i) {
            this.$set(i, 'searching', !i.searching)
        },
        searchByName (val) {
            // let arr = []
            let arr = this.basicData.filter(item => item.staff.name.indexOf(val) > -1)
            this.data = arr
        },
        exportFun () {
            this.exportVisible = true
        },
        closeExport () {
            this.exportVisible = false
        },
        exportDialogFun () {
            let str = ''
            str = this.secExpData.reduce((cal, cur) => {
                return cal ? (cal + ',') + cur.id : cur.id
            }, '')
            if (str.length > 0) {
                let url = `${window.hrPath}${performance.core}/pm/system/exportEvaObject?schemeid=` + this.schemeid + '&evagroupIds=' + str
                location.href = url
                this.exportVisible = false
            } else {
                // 请选择组
                this.$alert(
                    this._sn('pm.i18n1342'), {
                    type: 'warning'
                }
                )
            }
        },
        handleSelectFun (data) {
            this.secExpData = data
        },
        test () {
            this.$refs.exportTable.clearSelection()
        },
        afterRefGauge (data) {
            let lists = this.selectData.map(item => {
                return item.id
            })
            this.$emit('afterRefGauge', data, lists)
        },
        afterRefOrgs (data) {
            this.modelOrg = {
                refpk: '',
                refname: ''
            }
            this.$emit('afterObjectOrg', data, this.activeItem)
        },
        

        saveGauge (data) {
            let param = {
                schemeid: this.schemeid,
                evaobject_ids: this.selectData.map(item => {
                    return item.id
                }),
                gaugeid: data[0].refpk
            }
            fetchData(`${window.hrPath}${performance.core}/pm/system/saveEvaObjectGauge`, {
                type: 'Post',
                data: param
            }).then(res => {
                this.diaTableLoading = false
                if (res.statusCode === 200) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    })
                    this.chooseItem(this.activeItem, this.activeGroupname, this.activeIndex)
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            }).catch(err => {
                this.diaTableLoading = false
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: err.message
                })
            })
        }, 

        // qiuyue on 20201201
        // 导入
        importExcel (e) {
            var that = this
            that.upload(e, {
                folder: 'product'
            })
        }, 
        exportExcel () { 
            let url = `${window.hrPath}${performance.core}/pm/system/exportExcelVal?schemeid=` + this.schemeid 
            location.href = url
            // this.exportVisible = false
        }, 
        uploadToService (uploadData) {
            document.getElementById('uploadfile').value = null
            let str = ''
            let chooseGroup = ''
            str = this.listData.reduce((cal, cur) => {
                return cal ? (cal + ',') + cur.id : cur.id
            }, '')
            let json = uploadData
            if (str !== '') {
                 chooseGroup = this.activeItem
                if (!chooseGroup) {
                    chooseGroup = this.listData[0].id
                }
            }
            fetchData(
                `${window.hrPath}${performance.core}/pm/system/importExcelVal?schemeid=` + this.schemeid + '&groupChoose=' + chooseGroup + '&evagroupIds=' + str,
                {
                    type: 'post',
                    data: json
                }
                 )
                .then(res => {
                   let data = res
                   if (data.i18nCode === '002') {
                       // 导入失败，导出错误文件
                    let url = `${window.hrPath}${performance.core}/pm/system/exportErrObj?schemeid=` + this.schemeid 
                    location.href = url 
                    this.loading = false
                    this.$message({
                        type: 'error',
                        duration: 5000,
                        message: '导入失败，请到考核对象导入模板_校验失败.xlsx文件查看！'
                    })
                   } else {
                       this.$emit('updateGroup', res, chooseGroup)
                       this.loading = false
                   }
                })
                .catch(err => {
                    this.loading = false
                    this.$message({
                        type: 'error',
                        duration: 5000,
                        message: err
                    })
                    throw new Error(err)
                })
        },
        // 封装上传的方法
        upload (event, options) {
            let that = this
            let defaults = {
                folder: 'default',
                size: 8,
                type: ['xlsx', 'xls'], // 'word', 'pdf', 'zip', 'rar', 'sheet', 'text','log','psd'
                maxWidth: Number, // 最大宽度
                maxHeight: Number, // 最大高度
                success: false,
                beforeSend: false
            }
           // var opts = $.extend(defaults, options);
            let files = event.target.files 
            for (let i = 0; i < files.length; i++) {
                let file = files[i] 
                // 上传附件大于8M
                if (file.size > defaults.size * 1024 * 1024) {
                   this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: '文件【' + file.name + '】大于' + defaults.size + 'M!'
                })
                   return
                }
                let valiType = false
                for (let i = 0; i < defaults.type.length; i++) {
                   let item = defaults.type[i]
                   if (file.name.indexOf(item) >= 0) {
                       valiType = true
                       break
                   }
                }
                if (!valiType) {
                   this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: '上传文件类型不正确！'
                })
                   return
                } 
                let reader = new FileReader()
                // var type = file.type.split('/')[0]
                // 启动函数
                reader.readAsBinaryString(file)
                reader.onloadstart = function () {
                    that.loading = 'loading'
                }
                reader.onloadend = function (e) { 
                    // workbook存放excel的所有基本信息
                    const workbook = XLSX.read(e.target.result, {type: 'binary'})
                    // 定义sheetList中存放excel表格的sheet表，就是最下方的tab
                    let sheetList = workbook.SheetNames// 工作表名称集合
                    let uploadData = []
                    sheetList.forEach((name) => {
                        let worksheet = workbook.Sheets[name] // 只能通过工作表名称来获取指定工作表
                        let ws = XLSX.utils.sheet_to_json(worksheet) // 生成json表格内容
                        for (let i = 0; i < ws.length; i++) {
                        uploadData.push(ws[i])
                        }
                    })
                    // 将数据传到后台
                    that.uploadToService(uploadData)
                }
            }
        }
            // qiuyue -------------end
        }
}
</script>
<style lang="less" scoped>
#object-list {
    border: 1px solid rgba(78, 89, 104, 0.19);
    border-radius: 3px;
    /*min-height: 446px;*/
    position: relative;
    .group-name {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    .indiclass {
        padding: 10px;
        border-bottom: 1px solid rgba(78, 89, 104, 0.19);
        span {
            cursor: pointer;
            margin-right: 10px;
            &:last-of-type {
                float: right;
            }
        }
    }
    .add-group {
        width: 100%;
        text-align: center;
        position: absolute;
        bottom: 10px;
        left: 0;
        color: #007ace;
        cursor: pointer;
    }
    li {
        padding-left: 25px;
        padding-right: 25px;
        line-height: 36px;
        color: #474d54;
        cursor: pointer;
        display: -webkit-flex;
        display: -ms-flexbox;
        display: flex;
        -webkit-justify-content: space-between;
        -moz-justify-content: space-between;
        justify-content: space-between;
        &.active {
            color: #e14c46;
            background: #e1e1e1;
            .lifont {
                display: block;
            }
        }
        > div:not(.el-input) {
            width: 80px;
            i {
                float: right;
            }
        }
        &:hover {
            background: #e1e1e1;
            .lifont {
                display: block;
            }
        }
        .lifont {
            display: none;
        }
    }
}
.el-table {
    border: 1px solid #dfe3e8;
    border-radius: 3px;
}
.addicon {
    position: relative;
    float: right;
    top: 0;
    right: 0;
    p {
        line-height: 2;
        color: #007ace;
    }
    /*.el-autocomplete {*/
    /*position: absolute;*/
    /*opacity: 0;*/
    /*right: 0;*/
    /*top: 0;*/
    /*}*/
}
.list-header {
    background: #f3f3f3;
    height: 38px;
    line-height: 38px;
    padding-left: 15px;
    > span {
        margin-right: 14px;
        cursor: pointer;
    }
}
@media all and (min-width: 1920px) {
    #object-list {
        /*min-height: 622px;*/
    }
}

@media all and (min-width: 1441px) and (max-width: 1919px) {
    #object-list {
        /*min-height: 534px;*/
    }
}
@media all and (max-width: 1440px) {
}
</style>
<style lang="less">
#object-list {
    input {
        font-family: inherit;
    }
    li {
        input {
            height: 30px;
        }
        .iconfont {
            font-size: 18px;
            margin-left: 6px;
        }
    }
}
#pm-object {
    .addicon {
        .el-autocomplete {
            .el-input__icon {
                padding-left: 20px;
                padding-right: 20px;
                padding-top: 10px;
                margin-top: -5px;
            }
        }
    }
    .el-menu-item {
        border-bottom: none;
    }
    .el-menu--horizontal > .el-menu-item {
        height: 36px;
        line-height: 24px;
        &.is-active {
            border-bottom: none;
            &:after {
                content: '';
                position: absolute;
                bottom: 3px;
                left: 50%;
                margin-left: -7px;
                height: 6px;
                width: 14px;
                border-radius: 2px;
                background: #e14c46;
            }
        }
    }
    .el-table__empty-block {
        /*height: 100%;*/
    }
}
#pm-object {
    thead {
        .el-input--small .el-input__inner {
            height: 24px;
        }
        .el-input--small .el-input__icon {
            line-height: 24px;
        }
        .el-input {
            line-height: 14px;
        }
    }
    .header-expand {
        /*width: 100%;*/
        /*display: -webkit-flex;*/
        /*display: flex;*/
        /*-webkit-justify-content: space-between;*/
        /*justify-content: space-between;*/
        > span,
        > div {
            /*width: 50%;*/
            display: inline-block;
            padding: 0;
            &:first-child {
                text-align: left;
            }
            &:nth-of-type(2) {
                text-align: right;
                float: right;
            }
        }
        div {
            line-height: inherit;
            vertical-align: middle;
        }
    }
    .header-right {
        /*float: right*/
        position: absolute;
        right: 8px;
        top: calc(~'50% - 13px');
        padding: 6px;
        &.reference-com {
            margin-top: -5px;
            padding: 6px;
        }
    }
    .el-table .sort-caret.ascending {
        border-bottom-color: #d0d0d0;
    }
    .el-table .sort-caret.descending {
        border-top-color: #d0d0d0;
    }
    .el-table .descending .sort-caret.descending {
        border-top-color: #e14c46;
    }
    .el-table .ascending .sort-caret.ascending {
        border-bottom-color: #e14c46;
    }
}
</style>
