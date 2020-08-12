package pl.mjurek.notepage.dao.mysql.note;

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
import pl.mjurek.notepage.model.note.Note;
import pl.mjurek.notepage.model.note.ImportantState;
import pl.mjurek.notepage.service.note.SortOptions;
import pl.mjurek.notepage.model.note.StatusNote;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDAOImpl implements NoteDAO {
  private static final String CREATE =
      "INSERT INTO note(description,  date_id, user_id, important_state, status_note) "
          + "VALUES(:description, :date_id, :user_id, :important_state, :status_note);";

  private static final String READ =
      "SELECT note_id, description, note.date_id, note.user_id, status_note, important_state,"
          + "date.date_id, stick_note, deadline_note, user_made_task,"
          + "user.user_id,name,email FROM note JOIN date ON note.date_id=date.date_id"
          + " JOIN user ON note.user_id=user.user_id WHERE note_id=:note_id LIMIT 1;";

  private static final String UPDATE =
      "UPDATE note SET description=:description, date_id=:date_id, user_id=:user_id,"
          + " status_note=:status_note, important_state=:important_state WHERE note_id=:note_id;";
  private static final String UPDATE_DESCRIPTION_IMPORTANT_STATE =
      "UPDATE note SET description=:description,important_state=:important_state WHERE note_id=:note_id;";

  private static final String DELETE = "DELETE FROM note WHERE note_id=:note_id;";

  private static final String READ_ALL_BY_USER_ID =
      "SELECT note_id, description, note.date_id, user_id, status_note, important_state "
          + ",date.date_id, stick_note, deadline_note, user_made_task"
          + " FROM note JOIN date ON note.date_id=date.date_id WHERE user_id=:user_id;";

  private static final String GET_ALL_BY_STATUS =
      "SELECT note_id,description,note.date_id,user_id,status_note,important_state"
          + ",date.date_id,stick_note,deadline_note,user_made_task "
          + "FROM note JOIN date ON note.date_id=date.date_id"
          + " WHERE status_note=:status_note AND user_id=:user_id;";

  private final NamedParameterJdbcTemplate template;

  public NoteDAOImpl() {
    template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
  }

  @Override
  public Note create(Note note) throws AddObjectException {
    Note copyNote = note.toBuilder().build();
    KeyHolder keyHolder = new GeneratedKeyHolder();
    SqlParameterSource parameterSource = getSqlParamSource(copyNote);
    int update = template.update(CREATE, parameterSource, keyHolder);
    if (update > 0) {
      copyNote.setId(keyHolder.getKey().longValue());
    } else {
      String message =
          String.format(
              "Can't insert entity: %s  to database.Entity: %s", note.getClass(), note.toString());
      throw new AddObjectException(message);
    }
    return copyNote;
  }

  @Override
  public Note read(Long noteId) {
    SqlParameterSource paramSource = new MapSqlParameterSource("note_id", noteId);
    List<Note> result = template.query(READ, paramSource, new NoteFullRowMapper());
    return result.isEmpty() ? null : result.get(0);
  }

  @Override
  public Note update(Note note) throws UpdateObjectException {
    Note result = note.toBuilder().build();

    SqlParameterSource paramSource = getSqlParamSource(result);
    int update = template.update(UPDATE, paramSource);
    if (update < 1) {
      String message =
          String.format(
              "Can't update entity: %s  to database.Entity: %s", note.getClass(), note.toString());
      throw new UpdateObjectException(message);
    }
    return result;
  }

  @Override
  public Note update(long noteId, String description, String importantStatus)
      throws UpdateObjectException {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("note_id", noteId);
    paramMap.put("description", description);
    paramMap.put("important_state", importantStatus);

    SqlParameterSource paramSource = new MapSqlParameterSource(paramMap);
    int update = template.update(UPDATE_DESCRIPTION_IMPORTANT_STATE, paramSource);
    if (update < 1) {
      String message = String.format("Can't update entity  %d  to database.", noteId);
      throw new UpdateObjectException(message);
    }
    return Note.builder()
        .description(description)
        .importantState(ImportantState.valueOf(importantStatus.toUpperCase()))
        .build();
  }

  @Override
  public void delete(Long id) throws DeleteObjectException {
    MapSqlParameterSource paramSource = new MapSqlParameterSource("note_id", id);
    int update = template.update(DELETE, paramSource);
    if (update < 1) {
      throw new DeleteObjectException("Can't delete note.Entity: " + id);
    }
  }

  @Override
  public List<Note> getAll(long user_id) {
    SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", user_id);
    return template.query(READ_ALL_BY_USER_ID, parameterSource, new NoteRowMapper());
  }

  @Override
  public List<Note> getAll(long user_id, StatusNote state) {
    SqlParameterSource parameterSource = getSqlParamSource(user_id, state);
    return template.query(GET_ALL_BY_STATUS, parameterSource, new NoteRowMapper());
  }

  private SqlParameterSource getSqlParamSource(long user_id, StatusNote state) {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("user_id", user_id);
    paramMap.put("status_note", state.name());
    return new MapSqlParameterSource(paramMap);
  }

  @Override
  public List<Note> getAll(long user_id, SortOptions sortBy) {
    List<Note> result = null;
    try (Statement statement = ConnectionProvider.getConnection().createStatement()) {
      // elegant solution dont working
      // https://stackoverflow.com/questions/34760951/binding-value-in-orderby-not-working-with-namedparameterjdbctemplate
      String sql =
          "SELECT note_id,description,note.date_id,user_id,status_note,important_state"
              + ",date.date_id,stick_note,deadline_note,user_made_task "
              + "FROM note JOIN date ON note.date_id=date.date_id"
              + " WHERE user_id="
              + user_id
              + " ORDER BY "
              + sortBy.name().toLowerCase()
              + " ASC;";
      ResultSet set = statement.executeQuery(sql);

      result = makeList(set);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private List<Note> makeList(ResultSet set) throws SQLException {
    List<Note> result = new ArrayList<>();
    NoteRowMapper rowMapper = new NoteRowMapper();
    while (set.next()) {
      Note note = rowMapper.mapRow(set, set.getRow());
      result.add(note);
    }
    return result;
  }

  @Override
  public List<Note> getAll(long user_id, StatusNote state, SortOptions sortBy) {
    List<Note> result = null;
    try (Statement statement = ConnectionProvider.getConnection().createStatement()) {
      String sql =
          "SELECT note_id,description,note.date_id,user_id,status_note,important_state"
              + ",date.date_id,stick_note,deadline_note,user_made_task "
              + "FROM note JOIN date ON note.date_id=date.date_id"
              + " WHERE user_id="
              + user_id
              + " AND status_note='"
              + state.name()
              + "' ORDER BY "
              + sortBy.name().toLowerCase()
              + " ASC;";
      ResultSet set = statement.executeQuery(sql);

      result = makeList(set);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private SqlParameterSource getSqlParamSource(Note note) {
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("description", note.getDescription());
    paramMap.put("date_id", note.getDate().getId());
    paramMap.put("user_id", note.getUser().getId());
    paramMap.put("status_note", note.getStatusNote().name());
    paramMap.put("important_state", note.getImportantState().name());
    paramMap.put("note_id", note.getId());
    return new MapSqlParameterSource(paramMap);
  }

  private static class NoteFullRowMapper implements RowMapper<Note> {
    @Override
    public Note mapRow(ResultSet resultSet, int i) throws SQLException {
      DateNote date =
          DateNote.builder()
              .id(resultSet.getLong("date_id"))
              .dateStickNote(resultSet.getTimestamp("stick_note"))
              .dateDeadlineNote(resultSet.getTimestamp("deadline_note"))
              .dateUserMadeTask(resultSet.getTimestamp("user_made_task"))
              .build();
      User user =
          User.builder()
              .id(resultSet.getLong("user_id"))
              .name(resultSet.getString("name"))
              .email(resultSet.getString("email"))
              .build();

      return Note.builder()
          .id(resultSet.getLong("note_id"))
          .description(resultSet.getString("description"))
          .importantState(ImportantState.valueOf(resultSet.getString("important_state")))
          .date(date)
          .statusNote(StatusNote.valueOf(resultSet.getString("status_note")))
          .user(user)
          .build();
    }
  }

  private static class NoteRowMapper implements RowMapper<Note> {
    @Override
    public Note mapRow(ResultSet resultSet, int i) throws SQLException {
      DateNote date =
          DateNote.builder()
              .id(resultSet.getLong("date_id"))
              .dateStickNote(resultSet.getTimestamp("stick_note"))
              .dateDeadlineNote(resultSet.getTimestamp("deadline_note"))
              .dateUserMadeTask(resultSet.getTimestamp("user_made_task"))
              .build();

      return Note.builder()
          .id(resultSet.getLong("note_id"))
          .description(resultSet.getString("description"))
          .importantState(ImportantState.valueOf(resultSet.getString("important_state")))
          .date(date)
          .statusNote(StatusNote.valueOf(resultSet.getString("status_note")))
          .build();
    }
  }
}
