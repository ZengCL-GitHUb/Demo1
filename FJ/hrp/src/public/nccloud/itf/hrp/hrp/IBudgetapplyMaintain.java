
package nccloud.itf.hrp.hrp;

import java.util.List;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.hrp.budget_apply.AggBudgetApplyVO;
import nc.vo.hrp.budget_apply.BudgetByEditVO;
import nc.vo.hrp.budget_apply.DeptBudgetEditVO;
import nc.vo.hrp.budget_apply.OrgBudgetSubEditVO;
import nc.vo.hrp.budget_apply.PostBudgetEditVO;
import nc.vo.pub.BusinessException;

public interface IBudgetapplyMaintain {

        public void delete(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;

        public AggBudgetApplyVO[] insert(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;

        public AggBudgetApplyVO[] update(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;

        public AggBudgetApplyVO[] query(IQueryScheme queryScheme)
                        throws BusinessException;

        public AggBudgetApplyVO[] save(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;

        public AggBudgetApplyVO[] unsave(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;

        public AggBudgetApplyVO[] approve(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;

        public AggBudgetApplyVO[] unapprove(AggBudgetApplyVO[] clientFullVOs,
                        AggBudgetApplyVO[] originBills) throws BusinessException;
        
        /**
         *	 保存单位编制数据
         * @param vos
         * @throws BusinessException
         */
        public void saveBudget(BudgetByEditVO[] vos)throws BusinessException;
        
        /**
         * 	更新单位编制数据
         * @param vos
         * @throws BusinessException
         */
        public void updateBudget(List list)throws BusinessException;
        
        /**
         * 	回写编制信息
         * @param aggvo
         * @throws BusinessException
         */
        public void reback(AggBudgetApplyVO aggvo)throws BusinessException;
        
        /**
         * 	保存单位编制子表数据
         * @param vos
         * @throws BusinessException
         */
        public void saveOrgSubBudget(OrgBudgetSubEditVO[] vos)throws BusinessException;
        
        /**
         * 	更新单位编制子表数据
         * @param vos
         * @throws BusinessException
         */
        public void updateOrgSubBudget(OrgBudgetSubEditVO[] vos)throws BusinessException;
        
        /**
         * 	根据编制变更申请PK，删除单位，部门编制数据
         * @param pk_budget_apply
         * @throws BusinessException
         */
        public void deleteOrgBudgetByPk(String pk_budget_apply) throws BusinessException;
        
        /**
         *	 保存部门编制数据
         * @param vos
         * @throws BusinessException
         */
        public void saveDeptBudget(DeptBudgetEditVO[] vos)throws BusinessException;
        
        /**
         * 	保存部门编制子表数据
         * @param vos
         * @throws BusinessException
         */
        public void saveDeptSubBudget(PostBudgetEditVO[] vos)throws BusinessException;
        
        
}
