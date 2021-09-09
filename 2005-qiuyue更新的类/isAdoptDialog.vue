<template>
    <div class="isAdoptDialog"
         v-loading.fullscreen.lock="loading"
         element-loading-spinner="iconfont wui-Loading">
        <el-dialog top='65px'
                   :title="diaList[diaFlag].title"
                   :visible.sync="dialogFormVisible"
                   :width="diaFlag === 'rereview'? '40%': '80%'">
            <el-row>{{diaList[diaFlag].text}}</el-row>
            <el-table v-if="diaFlag === 'rereview'"
                      class="border-table"
                      border
                      :data="reviewGroups"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection"
                                 width="60">
                </el-table-column>
                <el-table-column align="left"
                                 prop="groupname"
                                 :label="_sn('pm.i18n324')">
                    <!--考核组-->
                </el-table-column>
            </el-table>
            <el-table :data="completeStaff"
                      v-else-if="dialogFormVisible"
                      class="border-table"
                      border
                      @selection-change="handleSelectionChange"
                      ref="multipleTable">
                <el-table-column type="selection"
                                 v-if="diaFlag !== 'completed' && diaFlag !== 'calend'"
                                 width="60">
                </el-table-column>
                <el-table-column align="left"
                                 prop="code"
                                 sortable
                                 :label="_sn('common.i18n11')"
                                 v-if="diaFlag !== 'completed'"
                                 show-overflow-tooltip>
                    <!--编码-->
                </el-table-column>
                <el-table-column align="left"
                                 prop="name"
                                 sortable
                                 :label="_sn('common.i18n24')"
                                 show-overflow-tooltip>
                    <!--姓名-->
                </el-table-column>
                <el-table-column align="left"
                                 prop="deptname"
                                 sortable
                                 :label="_sn('common.i18n20')"
                                 show-overflow-tooltip>
                    <!--部门-->
                </el-table-column>
                <el-table-column align="left"
                                 prop="mobile"
                                 sortable
                                 :label="_sn('pm.i18n325')"
                                 show-overflow-tooltip>
                    <!--手机-->
                </el-table-column>
                <el-table-column align="left"
                                 prop="email"
                                 sortable
                                 :label="_sn('pm.i18n82')"
                                 show-overflow-tooltip>
                    <!--邮箱-->
                </el-table-column>
            </el-table>
            <div slot="footer"
                 class="dialog-footer">
                <el-button type="text"
                           @click="dialogFormVisible = false">
                    <!--取消-->{{_sn('common.cancel')}}</el-button>
                <el-button type="primary"
                           @click="diaSure"
                           :loading="saving">
                    <!--确定-->{{_sn('common.i18n2')}}</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { fetchData } from '@/utils/util.js'
const { performance } = window.ServiceConfigs
export default {
    props: {
        getData: {
            type: Function,
            default: () => { }
        }
    },
    data () {
        return {
            loading: false,
            dialogFormVisible: false,
            diaList: {
                recomplete: {
                    title: this._sn('pm.i18n65'), // 重新填报
                    text: this._sn('pm.i18n340'), // <!--请选择需要重新填报PBC的人员，确认之后，所选人员只保留个人PBC数据，其PBC审核结果、以及PBC打分数据将删除，员工可修改原有的PBC，重新提交审核并打分。
                    warning: this._sn('pm.i18n341') // 请选择重新填报人员
                },
                rescore: {
                    title: this._sn('pm.i18n62'), // <!--重新评分-->
                    text: this._sn('pm.i18n342'), // <!--请选择需要重新评分的人员，确认之后，所选人员只保留第一轮评价分数，其余轮次分数，以及审核分数将删除，第一轮评分修改之后，可重新提交后续轮次打分，并进行审核。-->
                    warning: this._sn('pm.i18n343') // <!--请选择重新评分人员-->
                },
                rereview: {
                    title: this._sn('pm.i18n152'), // <!--重新审核-->
                    text: this._sn('pm.i18n344'), // <!--请选择需要重新审核的考核组，所有考核组只保留第一轮审核分数，其余轮次审核结果将删除。从第一轮开始重新审核。-->
                    warning: this._sn('pm.i18n345') // <!--请选择考核组-->
                },
                completed: {
                    title: this._sn('pm.i18n346'), // <!--结束填报-->
                    text: this._sn('pm.i18n347') // <!--还有PBC未完成，是否确认结束？如果确认强制结束，则这些未完成PBC的人员，无法进行后续的打分。-->
                },
                calend: {
                    title: this._sn('pm.i18n148'), // <!--评分结束-->
                    text: this._sn('pm.i18n348') // <!--还有评分未完成，是否确认结束？如果结束，未评分的考核人将按弃权处理。-->
                },
                startPro: {
                    title: this._sn('pm.i18n1128'), // <!--启动方案-->
                    text: '以下人员已失效或离职，选择后将从此方案中删除' // <!--以下人员已失效或离职，选择后将从此方案中删除-->
                },
                // qiuyue on 20201217 取消评分结束
                cancelMark: {
                    title: '取消评分结束',
                    text: '请选择需要取消评分结束的人员，确认之后，可以继续评分。' 
                }
            },
            saving: false,
            diaFlag: 'recomplete',
            completeStaff: [],
            reviewGroups: [],
            multipleSelection: []
        }
    },
    created () {

    },
    methods: {
        diaShow (id, flag) {
            this.schemeid = id
            this.diaFlag = flag
            let data = { id: id }
            this.loading = true
            switch (flag) {
                case 'rereview':
                    fetchData(
                        `${window.hrPath}${performance.pbc}/pm/flow/queryEvaGroupByScheme`,
                        {
                            type: 'get',
                            data
                        }
                    ).then(res => {
                        this.loading = false
                        if (res.statusCode === 200) {
                            this.reviewGroups = res.data
                            this.dialogFormVisible = true
                            // this.getData()
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    }).catch(() => {
                        this.loading = false
                    })
                    break
                case 'completed':
                    // let data = {'id': id}
                    fetchData(`${window.hrPath}${performance.pbc}/pm/flow/completed`, {
                        type: 'Post',
                        data
                    }).then(res => {
                        this.loading = false
                        if (res.statusCode === 200) {
                            if (res.data) {
                                this.completeStaff = res.data
                                this.dialogFormVisible = true
                            } else {
                                this.dialogFormVisible = false
                                this.$message({
                                    type: 'success',
                                    message: res.message
                                }) // <!--设置成功-->
                                this.getData()
                            }
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    }).catch(() => {
                        this.loading = false
                    })
                    break
                case 'calend':
                    fetchData(`${window.hrPath}${performance.pbc}/pm/flow/calculating`, {
                        type: 'Post',
                        data: { id: id, isforce: false }
                    }).then(res => {
                        // debugger
                        this.loading = false
                        if (res.statusCode === 200) {
                            if (res.data) {
                                this.completeStaff = res.data
                                this.dialogFormVisible = true
                            } else {
                                this.dialogFormVisible = false
                                this.$message({
                                    type: 'success',
                                    message: res.message
                                }) // <!--设置成功-->
                                this.getData()
                            }
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    }).catch(() => {
                        this.loading = false
                    })
                    break
                case 'startPro':
                    this.loading = true
                    fetchData(`${window.hrPath}${performance.pbc}/pm/flow/submitScheme`, {
                        type: 'Post',
                        data: { id: id }
                    }).then(res => {
                        this.loading = false
                        if (res.statusCode === 200) {
                            if (res.data) {
                                this.completeStaff = res.data
                                this.dialogFormVisible = true
                            } else {
                                this.dialogFormVisible = false
                                this.$message({
                                    type: 'success',
                                    message: res.message
                                }) // <!--设置成功-->
                                this.getData()
                            }
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
                    break
                default:
                    fetchData(
                        `${window.hrPath}${performance.pbc}/pm/flow/queryStaffForRefill`,
                        {
                            type: 'Post',
                            data
                        }
                    ).then(res => {
                        this.loading = false
                        if (res.statusCode === 200) {
                            this.completeStaff = res.data
                            this.dialogFormVisible = true
                        } else {
                            this.$message({
                                type: 'warning',
                                duration: 5000,
                                message: res.message
                            })
                        }
                    }).catch(() => {
                        this.loading = false
                    })
            }
            // if (flag === 'rereview') {
            //   fetchData(`${window.hrPath}${performance.pbc}/pm/flow/queryEvaGroupByScheme`, {
            //     type: 'get',
            //     data
            //   }).then(res => {
            //     if (res.statusCode === 200) {
            //       this.reviewGroups = res.data
            //     } else {
            //       this.$message({
            //         type: 'warning',
            // duration: 5000,
            //         message: res.message
            //       })
            //     }
            //   })
            // } else {
            //   fetchData(`${window.hrPath}${performance.pbc}/pm/flow/queryStaffForRefill`, {
            //     type: 'Post',
            //     data
            //   }).then(res => {
            //     if (res.statusCode === 200) {
            //       this.completeStaff = res.data
            //     } else {
            //       this.$message({
            //         type: 'warning',
            // duration: 5000,
            //         message: res.message
            //       })
            //     }
            //   })
            // }
        },
        /**
         * @des 点击确定按钮执行事件
         */
        diaSure () {
            // this.$refs.multipleTable.toggleRowSelection()
            this.saving = true
            if (this.multipleSelection.length > 0) {
                switch (this.diaFlag) {
                    case 'startPro':
                        this.startPro(this.schemeid)
                        break
                    case 'rescore':
                        this.reScore(this.schemeid)
                        break
                    case 'recomplete':
                        this.reComplete(this.schemeid)
                        break
                    case 'rereview':
                        this.reReview(this.schemeid)
                        break
                    case 'completed':
                        this.completed(this.schemeid)
                    // qiuyue on 20201217 取消评分结束
                        break
                    case 'cancelMark':
                        this.cancelMark(this.schemeid)
                }
            } else {
                if (this.diaFlag === 'startPro') {
                    this.startPro(this.schemeid)
                } else if (this.diaFlag === 'completed') {
                    this.completed(this.schemeid)
                } else if (this.diaFlag === 'calend') {
                    this.calScheme(this.schemeid)
                } else {
                    this.saving = false
                    // 填报结束只是提醒 不需要选择人员
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: this.diaList[this.diaFlag].warning
                    })
                }
            }
        },
        /**
         * @des 重新评分{{_sn('pm.i18n62')}}
         */
        reScore (id) {
            let lists = []
            this.multipleSelection.forEach(item => {
                lists.push(item.id)
            })
            // let data = {'id': id, data: lists}
            let data = { id: id, data: JSON.stringify(lists) }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/rescore`, {
                type: 'Post',
                data
            }).then(res => {
                this.saving = false
                if (res.statusCode === 200) {
                    this.dialogFormVisible = false
                    this.$message({
                        type: 'success',
                        message: res.message
                    }) // <!--设置成功-->{
                    this.getData()
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message || this._sn('pm.i18n103')
                    }) // <!--设置失败-->{
                }
            }).catch(err => {
                this.saving = false
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                throw new Error(err)
            })
        },
        /**
         * qiuyue 20201217 取消评分结束
         */
        cancelMark (id) {
            let lists = []
            this.multipleSelection.forEach(item => {
                lists.push(item.id)
            })
            // let data = {'id': id, data: lists}
            let data = { id: id, data: JSON.stringify(lists) }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/cancelMark`, {
                type: 'Post',
                data
            }).then(res => {
                this.saving = false
                if (res.statusCode === 200) {
                    this.dialogFormVisible = false
                    this.$message({
                        type: 'success',
                        message: res.message
                    }) // <!--设置成功-->{
                    this.getData()
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message || this._sn('pm.i18n103')
                    }) // <!--设置失败-->{
                }
            }).catch(err => {
                this.saving = false
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                throw new Error(err)
            })
        },
        /**
         * @des 重新填报
         */
        reComplete (id) {
            let data = { id: id, data: JSON.stringify(this.handleSeletionID()) }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/refill`, {
                type: 'Post',
                data
            }).then(res => {
                this.saving = false
                if (res.statusCode === 200) {
                    this.dialogFormVisible = false
                    this.$message({
                        type: 'success',
                        message: res.message
                    }) // <!--设置成功-->
                    this.getData()
                } else {
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: res.message || this._sn('pm.i18n103') // <!--设置失败-->
                    })
                }
            }).catch(err => {
                this.saving = false
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                throw new Error(err)
            })
        },
        reReview (id) {
            let data = { id: id, data: JSON.stringify(this.handleSeletionID()) }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/reexamined`, {
                type: 'Post',
                data
            }).then(res => {
                this.saving = false
                if (res.statusCode === 200) {
                    this.dialogFormVisible = false
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
                    }) // <!--设置失败-->
                }
            }).catch(err => {
                this.saving = false
                this.$message({
                    type: 'error',
                    duration: 5000,
                    message: err
                })
                throw new Error(err)
            })
        },
        /**
         * @des 填报结束{{_sn('pm.i18n312')}}
         */
        completed (id) {
            this.loading = true
            let data = { id: id, isforce: true }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/completed`, {
                type: 'Post',
                data
            }).then(res => {
                this.saving = false
                this.loading = false
                if (res.statusCode === 200) {
                    this.dialogFormVisible = false
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
            }).catch(err => {
                this.saving = false
                this.loading = false
                this.$message({
                    type: 'warning',
                    duration: 5000,
                    message: err.message
                })
            })
        },
        /**
         * @des 评分结束(开始计算)
         */
        calScheme (id) {
            this.loading = true
            let data = { id: id, isforce: true }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/calculating`, {
                type: 'Post',
                data
            })
                .then(res => {
                    this.loading = false
                    this.saving = false
                    if (res.statusCode === 200) {
                        this.dialogFormVisible = false
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
                    this.loading = false
                    this.saving = false
                    this.$message({
                        type: 'warning',
                        duration: 5000,
                        message: err.message
                    })
                })
        },
        /**
         * @des 启动方案
         */
        startPro (schemeid) {
            this.loading = true
            let param = {
                id: schemeid,
                isdel: '1',
                staffids: this.handleSeletionID()
            }
            fetchData(`${window.hrPath}${performance.pbc}/pm/flow/submitScheme`, {
                type: 'Post',
                data: param
            }).then(res => {
                this.loading = false
                if (res.statusCode === 200) {
                    if (res.data) {
                        this.completeStaff = res.data
                        this.dialogFormVisible = true
                    } else {
                        this.dialogFormVisible = false
                        this.$message({
                            type: 'success',
                            message: res.message
                        }) // <!--设置成功-->
                        this.getData()
                    }
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
        /**
         * @des 修改列表内容 - 回调
         */
        handleSelectionChange (val) {
            this.multipleSelection = val
        },
        /**
         * @des 收集选中人员的id
         */
        handleSeletionID () {
            let lists = []
            this.multipleSelection && this.multipleSelection.forEach(item => {
                lists.push(item.id)
            })
            return lists || []
        }
    }
}
</script>