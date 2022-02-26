package com.zyc.zdh.job;
import java.io.Serializable;
import java.util.Date;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.util.SpringContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class MyJobBean extends QuartzJobBean implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(MyJobBean.class);
	/**
	 *
	 */
	private static final long serialVersionUID = -8509585011462529939L;

	public static final String TASK_ID = "task_id";

	@Autowired
	private QuartzJobMapper quartzJobMapper;

	public QuartzJobMapper getQuartzJobMapper() {
		return quartzJobMapper;
	}

	public void setQuartzJobMapper(QuartzJobMapper quartzJobMapper) {
		this.quartzJobMapper = quartzJobMapper;
	}

	/**
	 * 按照间隔执行计划
	 * @param context
	 * @throws JobExecutionException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			logger.debug("执行: {}",jobDataMap.get(TASK_ID));
			String taskId = jobDataMap.getString(TASK_ID);
			if (taskId == null || taskId.trim().equals("")) {
				throw new Exception("任务id为空");
			}
			// 记录当前时间更新任务最后执行时间
			Date currentTime = new Date();

			QuartzJobMapper quartzJobMapper2 = this.quartzJobMapper;
			QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
			quartzJobInfo = quartzJobMapper2.selectByPrimaryKey(taskId);

			if(quartzJobInfo==null){
				logger.info("调度任务发现空的任务,任务id"+taskId);
				return;
			}

			switch (quartzJobInfo.getJob_type()) {
				case "SHELL":
					logger.info("调度任务[SHELL],开始调度");
					ShellJob.run(quartzJobInfo);
					break;
				case "JDBC":
					logger.info("调度任务[JDBC],开始调度");
					JdbcJob.run(quartzJobInfo);
					break;
				case "FTP":
					logger.info("调度任务[FTP],开始调度");
					FtpJob.run(quartzJobInfo);
					break;
				case "HDFS":
					logger.info("调度任务[HDFS],开始调度");
					HdfsJob.run(quartzJobInfo);
					break;
				case "EMAIL":
					logger.debug("调度任务[EMAIL],开始调度");
					EmailJob.run(quartzJobInfo);
					EmailJob.noticeEvent();
					break;
				default:
					ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
					JobCommon.insertLog(quartzJobInfo.getJob_id(), "ERROR",
							"无法找到对应的任务类型,请检查调度任务配置中的任务类型");
					logger.debug("无法找到对应的任务类型,请检查调度任务配置中的任务类型");
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

}
