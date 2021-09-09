<template>
    <div class="manipulate">
        <div v-if="row.schemestate === 1">
            <!--<span @click="edit(row.schememode, row.id)">&lt;!&ndash;编辑&ndash;&gt;{{_sn('common.i18n5')}}</span>-->
            <el-dropdown class="prot">
                <span class="el-dropdown-link operate green-font">
                    <!-- 修改 -->
                    {{_sn('pm.i18n1225')}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <!-- <el-dropdown-item>任务</el-dropdown-item> -->
                    <el-dropdown-item @click.native="edit(row.schememode, row.id,0)">
                        <!-- 方案定义 -->{{_sn('common.i18n81')}}</el-dropdown-item>
                    <el-dropdown-item @click.native="edit(row.schememode, row.id,1)">
                        <!--考核流程-->{{_sn('common.i18n77')}}</el-dropdown-item>
                    <el-dropdown-item @click.native="edit(row.schememode, row.id,2)">
                        <!-- 考核对象 -->{{_sn('common.i18n79')}}</el-dropdown-item>
                    <el-dropdown-item @click.native="edit(row.schememode, row.id,3)"
                                      v-if="row.isresultaudited">
                        <!-- 结果审核流程 -->{{_sn('pm.i18n1108')}}</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <span class="prot"
                  @click="diaShow(row.id, 'startPro')">
                <!--启动方案-->{{_sn('pm.i18n1128')}}</span>
            <span class="prot"
                  @click="delFun(row.id)">
                <!--删除-->{{_sn('common.i18n9')}}</span>
        </div>
        <div v-if="row.schemestate === 0">
            <!--设置已完成-->
            <span class="prot"
                  @click="cancelScheme(row.id)">
                <!--重新设置-->{{_sn('pm.i18n94')}}</span>
                <!-- 指标下达的方案需要显示'开始填报' -->
            <span class="prot"
                  @click="completing(row.id)"
                  v-if="row.isallowfillup || row.indisource === 2">
                <!--开始填报-->{{_sn('pm.i18n144')}}</span>
            <span class="prot"
                  @click="startScore(row.id)"
                  v-else>
                <!--开始评分-->{{_sn('pm.i18n147')}}</span>
        </div>
        <div v-if="row.schemestate === 2">
            <!--方案填报中-->
            <span class="prot"
                  @click="cancelScheme(row.id)">
                <!--重新设置-->{{_sn('pm.i18n94')}}</span>
            <span class="prot"
                  @click="diaShow(row.id, 'completed')">
                <!--填报结束-->{{_sn('pm.i18n312')}}</span>
            <el-dropdown class="prot">
                <span class="el-dropdown-link operate green-font">
                    <!-- 更多 -->
                    {{_sn('pm.i18n1018')}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="toRemind(row.id, 'completing')">
                        {{_sn('pm.i18n1336')}}</el-dropdown-item>
                    <!--填报进展-->
                    <el-dropdown-item @click.native="monitorFun(row)">{{_sn('pm.i18n1375')}}
                    </el-dropdown-item>
                    <!--考核监控-->
                    <el-dropdown-item v-if="row.schememode !== 20"
                                      @click.native="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}
                    </el-dropdown-item>
                    <!--复制方案-->
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div v-if="row.schemestate === 3">
            <!-- 填报已结束 -->
            <span class="prot"
                  @click="monitorFun(row)"
                  v-if="row.schememode === 20 && !row.isallowcalc">
                <!-- 考核监控 -->{{_sn('pm.i18n1375')}}</span>
            <template v-else>
                <span class="prot"
                      @click="diaShow(row.id, 'recomplete')"
                      v-if="row.isallowfillup">
                    <!--重新填报-->{{_sn('pm.i18n65')}}</span>
                <span class="prot"
                      @click="startScore(row.id)">
                    <!--开始回顾 ： 开始评分-->{{row.schememode === 20 ? '开始回顾' : _sn('pm.i18n147')}}</span>
                <!--<span>设置解冻</span> <span>开始评分</span>-->
                <el-dropdown class="prot">
                    <span class="el-dropdown-link operate green-font">
                        <!-- 更多 -->
                        {{_sn('pm.i18n1018')}}
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="middleFun(row.id)"
                                          v-if="row.isallowmtr">{{'中期回顾'}}
                        </el-dropdown-item>
                        <!-- 中期回顾 -->
                        <el-dropdown-item @click.native="monitorFun(row)">{{_sn('pm.i18n1375')}}
                        </el-dropdown-item>
                        <!--考核监控-->
                        <el-dropdown-item v-if="row.schememode !== 20"
                                          @click.native="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}
                        </el-dropdown-item>
                        <!--复制方案-->
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </div>
        <div v-if="row.schemestate === 4">
            <!--评分进行中-->
            <span class="prot"
                  @click="diaShow(row.id, 'recomplete')"
                  v-if="row.isallowfillup">
                <!--重新填报-->{{_sn('pm.i18n65')}}</span>
            <span class="prot"
                  @click="diaShow(row.id, 'calend')">
                <!--回顾结束 ：评分结束-->{{row.schememode === 20 ? '回顾结束' : _sn('pm.i18n148')}}</span>
            <span class="prot"
                  @click="toRemind(row.id, 'score')"
                  v-if="!row.isallowfillup">评分进展</span>
            <el-dropdown class="prot">
                <span class="el-dropdown-link operate green-font">
                    <!-- 更多 -->
                    {{_sn('pm.i18n1018')}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-if="row.isallowfillup"
                                      @click.native="toRemind(row.id, 'score')">
                        {{_sn('pm.i18n1382')}}</el-dropdown-item>
                    <!--评分进展-->
                    <el-dropdown-item @click.native="monitorFun(row)">{{_sn('pm.i18n1375')}}
                    </el-dropdown-item>
                    <!--考核监控-->
                    <el-dropdown-item v-if="row.schememode !== 20"
                                      @click.native="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}
                    </el-dropdown-item>
                    <!--复制方案-->
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div v-if="row.schemestate === 5">
            <!--评分已结束-->
            <!-- 中期回顾方案 -->
            <template v-if="row.schememode === 20">
                <span class="prot"
                      @click="monitorFun(row)">
                    {{_sn('pm.i18n1375')}}
                </span>
                <!--考核监控-->
            </template>
            <!-- 正常方案 -->
            <template v-if="row.schememode !== 20">
                <span class="prot"
                      @click="diaShow(row.id, 'rescore')">
                    <!--重新评分-->{{_sn('pm.i18n62')}}</span>

                <!-- qiuyue on 20201214 -->
                <span class="prot"
                      @click="diaShow(row.id, 'cancelMark')">
                    取消评分结束</span>
                <!-- qiuyue on 20201214 end-->
                
                <span class="prot"
                      v-if="row.isresultaudited"
                      @click="startReview(row.id)">
                    <!--开始审核-->{{_sn('pm.i18n84')}}</span>
                <span class="prot"
                      v-else
                      @click="topublish(row)">
                    <!--结果发布-->{{_sn('pm.i18n151')}}</span>
                <el-dropdown class="prot">
                    <span class="el-dropdown-link operate green-font">
                        <!-- 更多 -->
                        {{_sn('pm.i18n1018')}}
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="monitorFun(row)">
                            {{_sn('pm.i18n1375')}}
                        </el-dropdown-item>
                        <!--考核监控-->
                        <el-dropdown-item @click.native="copySchemeFun(row.id)">
                            {{_sn('pm.i18n1376')}}
                        </el-dropdown-item>
                        <!--复制方案-->
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </div>
        <div v-if="row.schemestate === 6">
            <!--结果审核中-->
            <!--<span @click="diaShow(row.id, 'rescore')">&lt;!&ndash;重新评分&ndash;&gt;{{_sn('pm.i18n62')}}</span>-->
            <span class="prot"
                  @click="cancelReview(row.id)">
                <!--取消审核-->{{_sn('common.cancel')}}{{_sn('pm.i18n828')}}</span>
            <span class="prot"
                  @click="endReview(row.id)">
                <!--审核结束-->{{_sn('pm.i18n321')}}</span>
            <el-dropdown class="prot">
                <span class="el-dropdown-link operate green-font">
                    <!-- 更多 -->
                    {{_sn('pm.i18n1018')}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="toRemind(row.id, 'review')">
                        <!--提醒-->{{_sn('common.i18n96')}}</el-dropdown-item>
                    <el-dropdown-item @click.native="monitorFun(row)">{{_sn('pm.i18n1375')}}
                    </el-dropdown-item>
                    <!--考核监控-->
                    <el-dropdown-item @click.native="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}
                    </el-dropdown-item>
                    <!--复制方案-->
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div v-if="row.schemestate === 7">
            <span class="prot"
                  @click="diaShow(row.id, 'rereview')">
                <!--重新审核-->{{_sn('pm.i18n152')}}</span>
            <span class="prot"
                  @click="topublish(row)">
                <!--结果发布-->{{_sn('pm.i18n151')}}</span>
            <el-dropdown class="prot">
                <span class="el-dropdown-link operate green-font">
                    <!-- 更多 -->
                    {{_sn('pm.i18n1018')}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="monitorFun(row)">{{_sn('pm.i18n1375')}}
                    </el-dropdown-item>
                    <!--考核监控-->
                    <el-dropdown-item @click.native="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}
                    </el-dropdown-item>
                    <!--复制方案-->
                    <el-dropdown-item @click.native="exportFun(row.id)">{{'结果导出'}}
                    </el-dropdown-item><!--结果导出-->
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <div v-if="row.schemestate === 8 && isover">
            <span class="prot"
                  v-if="!row.isresultconfirm"
                  @click="cancelRelease(row.id)">
                <!--取消发布-->{{_sn('pm.i18n153')}}</span>
            <span class="prot"
                  v-else
                  @click="toPress(row)">
                <!--认可进展-->{{'认可进展'}}</span>
            <span class="prot"
                  @click="monitorFun(row)">{{_sn('pm.i18n1375')}}</span>
            <!--考核监控-->
            <span class="prot"
                  @click="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}</span>
            <!--复制方案-->
        </div>
        <div v-if="row.schemestate === 8 && !isover">
            <span class="prot"
                  @click="toPress(row)">
                <!--认可进展-->{{'认可进展'}}</span>
            <span class="prot"
                  @click="overResult(row.id)">{{'结束考核'}}</span>
            <!--结束考核-->
            <el-dropdown class="prot">
                <span class="el-dropdown-link operate green-font">
                    <!-- 更多 -->
                    {{_sn('pm.i18n1018')}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="cancelRelease(row.id)">{{_sn('pm.i18n153')}}
                    </el-dropdown-item>
                    <!--取消发布-->
                    <el-dropdown-item @click.native="monitorFun(row)">{{_sn('pm.i18n1375')}}
                    </el-dropdown-item>
                    <!--考核监控-->
                    <el-dropdown-item @click.native="copySchemeFun(row.id)">{{_sn('pm.i18n1376')}}
                    </el-dropdown-item>
                    <!--复制方案-->
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        <!--<span size="mini" class="hover-icon" @click="add(row)">编辑</span>-->
        <!--<el-tooltip effect="dark" content="编辑" placement="bottom-start" :visible-arrow="false" :enterable="false">-->
        <!--<i @click="add(row)" class="iconfont wui-Copyreader"></i>-->
        <!--</el-tooltip>-->
        <!--<span size="mini" class="hover-icon" @click="del(row.id)">删除</span>-->
        <!--<span size="mini" class="hover-icon"  @click="stop(row)">{{row.enablestate? '停用': '启用'}}</span>-->
        <!--<el-tooltip effect="dark" :content="row.schemestate===1? '停用': '启用'" placement="bottom-start" :visible-arrow="false" :enterable="false">-->
        <!--<i @click="stop(row)" class="iconfont" :class="{'wui-tingyong':row.schemestate===1,'wui-zhongqi':row.schemestate===0}"></i>-->
        <!--</el-tooltip>-->

        <!-- 重新填报 等 查看不符合条件的人员弹框 -->
        <is-adopt-dialog ref="isAdopt"
                         :getData="getData"></is-adopt-dialog>
        <!-- 开始评分 || 结果发布 选择时间弹框 -->
        <el-dialog :visible.sync="scoreTime"
                   :title="dialogTitle"
                   :before-close="closeDiaTime"
                   :close-on-click-modal="false">
            <el-row>
                <el-form :inline="true"
                         ref="scorTime"
                         :model="SchemePlan">
                    <el-form-item :label="_sn('common.i18n27')"
                                  prop="sdate"
                                  :rules="[{ required: true, message: '请输入开始时间'}]">
                        <el-date-picker v-model="SchemePlan.sdate"
                                        type="date"
                                        disabled
                                        value-format="timestamp"
                                        :clearable="false"
                                        :editable="false"
                                        prefix-icon="iconfont wui-Calendar_bold"
                                        :placeholder="_sn('pm.i18n1027')">
                        </el-date-picker>
                    </el-form-item>
                    <!-- 结束日期 -->
                    <!-- 选择日期时间 -->
                    <div
                         style="width: 8px;height: 1px;background: rgb(208, 208, 208);display: inline-block;margin-right:10px;">
                    </div>
                    <el-form-item :label="_sn('common.i18n35')"
                                  prop="edate">
                        <el-date-picker v-model="SchemePlan.edate"
                                        type="date"
                                        :clearable="false"
                                        :editable="false"
                                        value-format="timestamp"
                                        prefix-icon="iconfont wui-Calendar_bold"
                                        :placeholder="_sn('pm.i18n1027')">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
            </el-row>
            <div slot="footer"
                 class="dialog-footer">
                <el-button type="text"
                           style="margin-left:4px;"
                           @click="closeDiaTime">
                    <!--取消-->{{_sn('common.cancel')}}</el-button>
                <el-button type="primary"
                           @click="saveSchemePlan"
                           :loading.inlineText="savingTime">
                    <!--保存-->{{_sn('common.i18n3')}}</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import IsAdoptDialog from '../isAdoptDialog'
import { fetchData } from '@/utils/util.js'
const { performance } = window.ServiceConfigs
export default {
    components: {
        IsAdoptDialog
    },
    props: {
        row: {
            type: Object,
            default: () => { }
        },
        edit: {
            type: Function,
            default: () => { }
        },
        getData: {
            type: Function,
            default: () => { }
        },
        isover: {
            type: Boolean,
            default: false
        },
        loadCallback: {
            type: Function,
            default: () => { }
        }
    },
    data () {
        return {
            scoreTime: false,
            savingTime: false,
            SchemePlan: {
                sdate: new Date(new Date().toLocaleDateString()).getTime(),
                edate: ''
            },
            loading: false,
            dialogTitle: '设置评分时间',
            timeType: '',
            publishId: '',
            scoreId: ''
        }
    },
    methods: {
        // 查看不符合人员弹框 - 重新填报等
        diaShow (id, flag) {
            this.$refs.isAdopt.diaShow(id, flag)
        },
        // 删除
        delFun (id) {
            // 是否执行删除？
            this.$confirm(this._sn('pm.i18n295'), '', {
                confirmButtonText: this._sn('common.i18n2'), // <!--确定-->
                cancelButtonText: this._sn('common.cancel'), // <!--取消-->
                type: 'warning',
                confirmButtonClass: 'el-button--warning'
            }).then(() => {
                fetchData(`${window.hrPath}${performance.pbc}/pm/flow/delScheme`, {
                    type: 'Post',
                    data: { id: id }
                })
                    .then(res => {
                        if (res.statusCode === 200) {
                            this.$message({
                                type: 'success',
                                message: this._sn('common.i18n55')
                            }) // <!--删除成功-->
                            this.getData()
                        } else {
                            this.loadCallback(false)
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    })
                    .catch(err => {
                        this.loadCallback(false)
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: err.message
                        })
                    })
            })
        },
        /**
         * @des 重新设置
         */
        cancelScheme (id) {
            // <!--重新设置-->{{_sn('pm.i18n94')}}
            let data = { id: id }
            // 重新设置，如果调整了考核对象，该考核对象的汇总结果将被删除，请确认是否重新设置？-->
            this.$confirm('确定要重新设置？', {
                confirmButtonText: this._sn('common.i18n2'), // '<!--确定-->
                cancelButtonText: this._sn('common.cancel'), // <!--取消-->
                confirmButtonClass: 'el-button--warning',
                type: 'warning'
            }).then(() => {
                this.loadCallback(true)
                fetchData(`${window.hrPath}${performance.pbc}/pm/flow/cancelScheme`, {
                    type: 'Post',
                    data
                })
                    .then(res => {
                        this.loadCallback(false)
                        if (res.statusCode === 200) {
                            this.$message({
                                type: 'success',
                                message: res.message
                            })
                            this.getData()
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    })
                    .catch(err => {
                        this.loadCallback(false)
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: err.message
                        })
                    })
            })
        },
        /**
         * @des 开始填报
         */
        completing (id) {
            // <!--开始填报-->{{_sn('pm.i18n144')}}
            this.loadCallback(true)
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/completing`, {
                type: 'Post',
                data: { id: id }
            })
                .then(res => {
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        }) // <!--设置成功-->
                        this.getData()
                    } else {
                        this.loadCallback(false)
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                })
                .catch(err => {
                    this.loadCallback(false)
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 开始评分
         */
        startScore (id) {
            this.dialogTitle = '设置评分时间'
            this.timeType = 'score'
            this.scoreTime = true
            this.scoreId = id
        },
        /**
         * @des 
         */
        toRemind (programId, flag) {
            let rou = flag === 'review' ? 'Pmremind' : 'Pmprogress'
            this.$router.push({ name: rou, query: { programId, flag } })
        },
        /**
         * @des 考核监控
         */
        monitorFun (row) {
            this.$router.push({
                name: 'Monitor',
                query: { id: row.id, isallowmtr: row.isallowmtr, ispublished: row.ispublished ? 1 : 0 }
            })
        },
        /**
         * @des 复制方案
         */
        copySchemeFun (id) {
            this.$router.push({ name: 'Pmedit', query: { browser: 0, oldSchemeId: id } })
        },
        /**
         * @des 中期回顾
         */
        middleFun (id) {
            this.$router.push({ name: 'Pmedit', query: { browser: 0, programId: id, from: 'middle' } })
        },
        /**
         * @des 开始审核
         */
        startReview (id) {
            this.loadCallback(true)
            // <!--开始审核-->{{_sn('pm.i18n84')}}
            let data = { id: id }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/initAudit`, {
                type: 'Post',
                data
            })
                .then(res => {
                    this.loadCallback(false)
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        }) // <!--设置成功-->
                        this.getData()
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                })
                .catch(err => {
                    this.loadCallback(false)
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 结果发布-判断是否需要确认时间
         */
        topublish (obj) {
            if (obj.isresultconfirm === true) {
                this.dialogTitle = '设置确认时间'
                this.timeType = 'publish'
                this.scoreTime = true
                this.publishId = obj.id
            } else {
                this.publish(obj.id)
            }
        },
        /**
         * @des 结果发布
         */
        publish (id, type) {
            if (type) {
                this.closeDiaTime()
            }
            this.loadCallback(true)
            let data = { id: id }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/publish`, {
                type: 'Post',
                data
            })
                .then(res => {
                    this.loadCallback(false)
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: this._sn('pm.i18n356')
                        }) // <!--发布成功-->
                        this.getData()
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                })
                .catch(err => {
                    this.loadCallback(false)
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 取消发布
         */
        cancelRelease (id) {
            // 取消发布
            this.loadCallback(true)
            // 确认取消发布？
            this.$confirm(this._sn('pm.i18n1219'), '', {
                confirmButtonText: this._sn('common.i18n2'), // <!--确定-->
                cancelButtonText: this._sn('common.cancel'), // <!--取消-->
                type: 'warning',
                confirmButtonClass: 'el-button--warning'
            }).then(() => {
                let data = { id: id }
                fetchData(`${window.hrPath}${performance.pbc}/pm/flow/cancelPublic`, {
                    type: 'Post',
                    data
                })
                    .then(res => {
                        this.loadCallback(false)
                        if (res.statusCode === 200) {
                            this.$message({
                                type: 'success',
                                message: this._sn('pm.i18n357') // <!--已取消发布-->
                            })
                            this.getData()
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    })
                    .catch(err => {
                        this.loadCallback(false)
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: err.message
                        })
                    })
            }).catch(() => {
                this.loadCallback(false)
            })
            // let data = {'id': id}
            // this.$confirm('取消发布之后，员工无法查看个人考核结果，主管也无法查看分管人群考核结果，是否确认取消发布？？', {
            //   confirmButtonText: '<!--确定-->{{_sn('common.i18n2')}}',
            //   cancelButtonText: '<!--取消-->{{_sn('common.cancel')}}',
            //   confirmButtonClass: 'el-button--warning',
            //   type: 'warning'
            // }).then(() => {
            //   fetchData(`${window.hrPath}${performance.pbc}/pm/flow/cancelScheme`, {
            //     type: 'Post',
            //     data
            //   }).then(res => {
            //     if (res.statusCode === 200) {
            //       this.$message({
            //         type: 'success',
            //         message: res.message
            //       })
            //       this.getData()
            //     } else {
            //       this.$message({
            //         type: 'warning',
            // duration: 5000,
            //         message: res.message
            //       })
            //     }
            //   })
            // }).catch(() => {
            // })
        },
        /**
         * @des 设置完成 * 现在已经被注释
         */
        stop (item) {
            let data = item
            // this.$refs['child'].submit('stop') 无效 此时子组件没有加载 child 为
            item.enablestate = !item.enablestate
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/saveScoreRule`, {
                type: 'Post',
                data
            }).then(res => {
                if (res.statusCode === 200) {
                    // let enableflag = res.data.enablestate ? '<!--已启用-->{{_sn('common.i18n65')}}' : '<!--已停用-->{{_sn('common.i18n95')}}'
                    // this.$message({
                    //   type: 'success',
                    //   message: enableflag
                    // })
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            })
        },
        /**
         * @des 开始评分 - 选择时间后弹框关闭事件
         */
        closeDiaTime (done) {
            this.SchemePlan = {
                sdate: new Date(new Date().toLocaleDateString()).getTime(),
                edate: ''
            }
            this.$refs['scorTime'].resetFields()
            this.scoreTime = false
            this.savingTime = false
            this.loadCallback(false)
            // done()
        },
        /**
         * @des 开始评分后 - 选择时间后弹框确定事件
         */
        saveSchemePlan () {
            this.$refs['scorTime'].validate((valid) => {
                if (valid) {
                    this.savingTime = true
                    let data = {
                        sdate: this.SchemePlan.sdate,
                        edate: this.SchemePlan.edate,
                        schemeid: this.timeType === 'publish' ? this.publishId : this.scoreId,
                        workstep: this.timeType === 'publish' ? 40 : 20
                    }
                    fetchData(`${window.hrPath}${performance.core}/pm/system/saveSchemePlan`, {
                        type: 'Post',
                        data
                    }).then(res => {
                        if (res.statusCode === 200) {
                            if (this.timeType === 'score') {
                                this.handleStart(this.scoreId)
                            } else if (this.timeType === 'publish') {
                                this.publish(this.publishId, true)
                            }
                        } else {
                            this.savingTime = false
                            this.$message({
                                type: 'error',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    }).catch(err => {
                        this.savingTime = false
                        this.$message({
                            type: 'error',
                            duration: 5000,
                            message: err
                        })
                        throw new Error(err)
                    })
                } else {
                    return false
                }
            })
        },
        /**
         * @des 处理 - 开始评分
         */
        handleStart (id) {
            // this.loadCallback(true)
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/startScore`, {
                type: 'Post',
                data: { id: id }
            }).then(res => {
                this.savingTime = false
                if (res.statusCode === 200) {
                    this.$message({
                        type: 'success',
                        message: res.message
                    }) // <!--设置成功-->
                    this.closeDiaTime()
                    this.getData()
                } else {
                    this.savingTime = false
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message
                    })
                }
            }).catch(err => {
                this.savingTime = false
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                throw new Error(err)
            })
        },
        /**
         * @des 取消审核
         */
        cancelReview (id) {
            this.loadCallback(true)
            let data = { id: id }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/cancelAudit`, {
                type: 'Post',
                data
            })
                .then(res => {
                    this.loadCallback(false)
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        }) // <!--设置成功-->
                        this.getData()
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                })
                .catch(err => {
                    this.loadCallback(false)
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 审核结束
         */
        endReview (id) {
            this.loadCallback(true)
            let data = { id: id }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/endAudit`, {
                type: 'Post',
                data
            })
                .then(res => {
                    this.loadCallback(false)
                    if (res.statusCode === 200) {
                        this.$message({
                            type: 'success',
                            message: res.message
                        }) // <!--设置成功-->
                        this.getData()
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                })
                .catch(err => {
                    this.loadCallback(false)
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 结束考核
         */
        overResult (id, flag = false) {
            this.loading = true
            let data = { schemeid: id, isforce: flag }
            fetchData(`${window.hrPath}${performance.pbc}/pm/result/overResultConfirm`, {
                type: 'Post',
                data
            })
                .then(res => {
                    this.loading = false
                    if (res.statusCode === 200) {
                        if (res.data && res.data.status) {
                            this.isForceFun(id, res.data.message)
                        } else {
                            this.$message({
                                type: 'success',
                                message: res.message
                            })
                            this.getData()
                        }
                    } else {
                        this.$message({
                            type: 'warning',
                            duration: 5000,
                            message: res.message
                        })
                    }
                })
                .catch(err => {
                    this.loading = false
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 确认是否强制结束考核
         */
        isForceFun (id, msg) {
            let tips = msg + `</br>确认是否强制结束考核`
            this.$confirm(tips, '', {
                confirmButtonText: this._sn('common.i18n2'), // <!--确定-->
                cancelButtonText: this._sn('common.cancel'), // <!--取消-->
                type: 'warning',
                dangerouslyUseHTMLString: true,
                confirmButtonClass: 'el-button--warning'
            }).then(() => {
                this.overResult(id, true)
            }).catch(() => {
                console.log('已取消')
            })
        },
        /**
         * @des 跳转 - 认可进展
         */
        toPress (obj) {
            this.$router.push({
                name: 'confirmexport',
                query: {
                    schemeid: obj.id,
                    gradeid: obj.gradeid,
                    isover: this.isover ? 'true' : 'false'
                }
            })
        },
        exportFun (id) {
            // 确定要导出吗？ 提示
            this.$confirm(this._sn('pm.i18n1426'), this._sn('common.i18n6')).then(() => {
                window.open(`${window.hrPath}${performance.pbc}/pm/flow/exportEvaObjectBySchemeId?schemeid=${id}`)
            }).catch(() => {
                console.log('取消导出')
            })
        }

    }
}
</script>

<style lang="less" scoped>
.manipulate {
    .prot {
        cursor: pointer;
        color: #007ace;
        margin-right: 10px;
        white-space: nowrap;
        right: inherit;
        top: inherit;
        &:last-child {
            margin-right: 0;
        }
        &.el-dropdown {
            position: inherit;
        }
    }
}
</style>

<style lang="less">
.el-message-box .el-message-box__status+.el-message-box__message{
    overflow: auto;
}
</style>