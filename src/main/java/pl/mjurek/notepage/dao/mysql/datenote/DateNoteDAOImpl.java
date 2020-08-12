package pl.mjurek.notepage.dao.mysql.datenote;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.datenote.DateNote;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DateNoteDAOImpl implements DateNoteDAO {
  private static final String CREATE =
      "INSERT INTO date(stick_note,deadline_note) VALUES(:stick_note,:deadline_note);";
  private static final String READ =
      "SELECT date_id,stick_note,deadline_note,user_made_task from date WHERE date_id=:date_id LIMIT 1;";
  private static final String UPDATE =
      "UPDATE date SET deadline_note=:deadline_note, user_made_task=:user_made_task "
          + "WHERE date_id=:date_id;";
  private static final String UPDATE_DEADLINE =
      "UPDATE date SET deadline_note=:deadline_note WHERE date_id=:date_id;";
  private static final String DELETE = "DELETE FROM date WHERE date_id=:date_id;";

  private final NamedParameterJdbcTemplate template;

  public DateNoteDAOImpl() {
    template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
  }

  @Override
  public DateNote create(DateNote dateNote) throws AddObjectException {
    DateNote copyDate = null;
    KeyHolder keyHolder = new GeneratedKeyHolder();
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("stick_note", dateNote.getDateStickNote());
    paramMap.put("deadline_note", dateNote.getDateDeadlineNote());

    SqlParameterSource parameterSource = new MapSqlParameterSource(paramMap);
    int update = template.update(CREATE, parameterSource, keyHolder);
    if (update > 0) {
      copyDate = copyAndUpdate(dateNote, keyHolder);
    } else {
      String message =
          String.format(
              "Can't create entity: %s  to database.Entity: %s",
              dateNote.getClass(), dateNote.toString());
      throw new AddObjectException(message);
    }
    return copyDate;
  }

  private DateNote copyAndUpdate(DateNote dateNote, KeyHolder keyHolder) {
    return DateNote.builder()
        .id(keyHolder.getKey().longValue())
        .dateStickNote(dateNote.getDateStickNote())
        .dateDeadlineNote(dateNote.getDateDeadlineNote())
        .dateUserMadeTask(dateNote.getDateUserMadeTask())
        .build();
  }

  @Override
  public DateNote read(Long dateId) {
    SqlParameterSource paramSource = new MapSqlParameterSource("date_id", dateId);
    DateNote dateNote = template.queryForObject(READ, paramSource, new DateNoteRowMapper());
    return dateNote;
  }

  @Override
  public DateNote update(DateNote dateNote) throws UpdateObjectException {
    DateNote result = dateNote.toBuilder().build();

    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("date_id", dateNote.getId());
    paramMap.put("deadline_note", dateNote.getDateDeadlineNote());
    paramMap.put("user_made_task", dateNote.getDateUserMadeTask());

    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int update = template.update(UPDATE, paramSource);
    if (update < 1) {
      String message =
          String.format(
              "Can't update entity: %s  to database.Entity: %s",
              dateNote.getClass(), dateNote.toString());
      throw new UpdateObjectException(message);
    }
    return result;
  }

  @Override
  public void update(long dateId, Timestamp deadline) throws UpdateObjectException {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("date_id", dateId);
    paramMap.put("deadline_note", deadline);

    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int update = template.update(UPDATE_DEADLINE, paramSource);
    if (update < 1) {
      String message = "Can't update DateNote entity";
      throw new UpdateObjectException(message);
    }
  }

  @Override
  public void delete(Long key) throws DeleteObjectException {
    MapSqlParameterSource paramSource = new MapSqlParameterSource("date_id", key);

    int update = template.update(DELETE, paramSource);
    if (update < 1) {
      String message = "Can't delete DateNote entity";
      throw new DeleteObjectException(message);
    }
  }

  private static class DateNoteRowMapper implements RowMapper<DateNote> {
    @Override
    public DateNote mapRow(ResultSet resultSet, int i) throws SQLException {
      return DateNote.builder()
          .id(resultSet.getLong("date_id"))
          .dateStickNote(resultSet.getTimestamp("stick_note"))
          .dateDeadlineNote(resultSet.getTimestamp("deadline_note"))
          .dateUserMadeTask(resultSet.getTimestamp("user_made_task"))
          .build();
    }
  }
}
