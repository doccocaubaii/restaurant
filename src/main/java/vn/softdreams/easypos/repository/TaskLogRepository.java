package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.TaskLog;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the TaskLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {
    List<TaskLog> findAllByComIdAndTypeAndStatus(Integer comId, String type, Integer status);
    Optional<TaskLog> findOneById(Integer id);
    Optional<TaskLog> findByComIdAndContent(Integer comId, String content);

    @Query(
        value = "select * from task_log tg where tg.status = 0 or ( tg.status = 2 and  tg.error_message like 'I/O%')",
        nativeQuery = true
    )
    List<TaskLog> findAllTaskLogErrorIO();

    @Query(value = "select * from task_log tg where tg.com_id = ?1 and  tg.id in ?2", nativeQuery = true)
    List<TaskLog> findAllByComIdAndIds(Integer comId, List<Integer> ids);

    @Query(value = "select * from task_log t where t.com_id in ?1 and t.type in ?2 and t.status in (0,2)", nativeQuery = true)
    List<TaskLog> findAllErrorAsyncByComIdsAndTypes(List<Integer> ids, List<String> type);

    @Query(value = "select * from task_log tg where tg.id in ?1", nativeQuery = true)
    List<TaskLog> findAllErrorByAndIds(List<Integer> ids);

    @Query(value = "select top(1) * from task_log where com_id = ?1 order by update_time desc", nativeQuery = true)
    Optional<TaskLog> findOneByComId(Integer comId);

    @Query(value = "select * from task_log where com_id = ?1 and type = ?2 ", nativeQuery = true)
    Optional<TaskLog> findIdByComIdAndType(Integer comId, String type);

    @Query(value = "select com_id  from task_log where id = ?1", nativeQuery = true)
    Integer findComIdById(Integer id);

    @Query(value = "select c.service from task_log t join company c on t.com_id = c.id where t.id = ?1", nativeQuery = true)
    String getServiceById(String id);

    @Query(value = "SELECT * FROM task_log WHERE create_time < DATEADD(MONTH, -4, GETDATE())", nativeQuery = true)
    List<TaskLog> findTaskLogBeforeFourMonths();

    @Modifying
    @Query(value = "delete FROM task_log WHERE create_time < DATEADD(MONTH, -4, GETDATE())", nativeQuery = true)
    void deleteTaskLogBeforeFourMonths();
}
