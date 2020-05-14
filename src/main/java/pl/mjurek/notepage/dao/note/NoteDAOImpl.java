package pl.mjurek.notepage.dao.note;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.ImportantState;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.StatusNote;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDAOImpl implements NoteDAO {
    private static final String CREATE =
            "INSERT INTO note(description,  date_id, user_id, important_state, status_note)" +
                    "VALUES(:description, :date_id, :user_id, :important_state, :status_note);";
    private static final String READ =
            "SELECT note_id, description, note.date_id, user_id, status_note, important_state " +
                    ",date.date_id, date_stick_note, date_deadline_note, date_user_made_task" +
                    " FROM note JOIN date ON note.date_id=date.date_id WHERE user_id=:user_id;";
    private static final String UPDATE =
            "UPDATE note SET description=:description, date_id=:date_id, user_id=:user_id," +
                    " status_note=:status_note, important_state=:important_state WHERE note_id=:note_id;";
    private static final String GET_ALL_BY_STATUS =
            "SELECT note_id,description,note.date_id,user_id,status_note,important_state" +
                    ",date.date_id,date_stick_note,date_deadline_note,date_user_made_task " +
                    "FROM note JOIN date ON note.date_id=date.date_id" +
                    " WHERE status_note=:status_note AND user_id=:user_id;";

    private static final String DELETE =
            "DELETE FROM note WHERE note_id=:note_id;";

    private NamedParameterJdbcTemplate template;

    public NoteDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }

    @Override
    public List<Note> getAll(long user_id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", user_id);
        return template.query(READ, parameterSource, new NoteRowMapper());
    }

    @Override
    public List<Note> getAll(long user_id, StatusNote state) {
        SqlParameterSource parameterSource = getSqlParamSource(user_id, state);
        return template.query(GET_ALL_BY_STATUS, parameterSource, new NoteRowMapper());
    }

    private SqlParameterSource getSqlParamSource(long user_id, StatusNote state) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("user_id", user_id);
        paramMap.put("status_note", state.name());
        return new MapSqlParameterSource(paramMap);
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
            throw new AddObjectException();
        }
        return copyNote;
    }

    private SqlParameterSource getSqlParamSource(Note note) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("description", note.getDescription());
        paramMap.put("date_id", note.getDate().getId());
        paramMap.put("user_id", note.getUser().getId());
        paramMap.put("status_note", note.getStatusNote().name());
        paramMap.put("important_state", note.getImportantState().name());
        return new MapSqlParameterSource(paramMap);
    }

    @Override
    public Note read(Long noteId) {
        SqlParameterSource paramSource = new MapSqlParameterSource("note_id", noteId);
        Note note = template.queryForObject(READ, paramSource, new NoteRowMapper());
        return note;
    }

    @Override
    public Note update(Note updateNote) {
        Note result = updateNote.toBuilder().build();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource = getSqlParamSource(result);
        int update = template.update(UPDATE, paramSource, keyHolder);
        if (update > 0) {
            result.setId(keyHolder.getKey().longValue());
        }
        return result;
    }

    @Override
    public void delete(Long key) throws DeleteObjectException {
        MapSqlParameterSource paramSource = new MapSqlParameterSource("note_id", key);
        int update = template.update(DELETE, paramSource);
        if (update < 1) {
            throw new DeleteObjectException();
        }
    }

    private class NoteRowMapper implements RowMapper<Note> {
        @Override
        public Note mapRow(ResultSet resultSet, int i) throws SQLException {
            DateNote date = DateNote.builder()
                    .id(resultSet.getLong("date_id"))
                    .dateStickNote(resultSet.getTimestamp("date_stick_note"))
                    .dateDeadlineNote(resultSet.getTimestamp("date_deadline_note"))
                    .dateUserMadeTask(resultSet.getTimestamp("date_user_made_task"))
                    .build();
            Note note = Note.builder()
                    .id(resultSet.getLong("note_id"))
                    .description(resultSet.getString("description"))
                    .importantState(ImportantState.valueOf(resultSet.getString("important_state")))
                    .date(date)
                    .statusNote(StatusNote.valueOf(resultSet.getString("status_note")))
                    .build();

            return note;
        }
    }
}

