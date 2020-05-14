package pl.mjurek.notepage.dao.note;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.mjurek.notepage.exception.CantAddObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.ImportantState;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.StatusNote;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDAOImpl implements NoteDAO {
    private static final String CREATE_NOTE =
            "INSERT INTO note(description,date_id,user_id,important_state)" +
                    "VALUES(:description,:date_id,:user_id,:important_state);";
    private static final String GET_ALL =
            "SELECT note_id,description,note.date_id,user_id,status_note,important_state" +
                    ",date.date_id,date_stick_note,date_deadline_note,date_user_made_task" +
                    " FROM note JOIN date ON note.date_id=date.date_id WHERE user_id=12;";

    private NamedParameterJdbcTemplate template;

    public NoteDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }

    @Override
    public List<Note> getAll(long user_id) {
        return template.query(GET_ALL, new NoteRowMapper());
    }

    @Override
    public Note create(Note note) throws CantAddObjectException {
        Note copyNote = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("description", note.getDescription());
        paramMap.put("date_id", note.getDate().getId());
        paramMap.put("user_id", note.getUser().getId());
        paramMap.put("important_state", note.getImportantState().name());

        SqlParameterSource parameterSource = new MapSqlParameterSource(paramMap);
        int update = template.update(CREATE_NOTE, parameterSource, keyHolder);
        if (update > 0) {
            copyNote = copyAndUpdate(note, keyHolder);
        } else {
            throw new CantAddObjectException();
        }
        return copyNote;
    }

    private Note copyAndUpdate(Note note, KeyHolder keyHolder) {
        return Note.builder()
                .id(keyHolder.getKey().longValue())
                .user(note.getUser())
                .date(note.getDate())
                .importantState(note.getImportantState())
                .description(note.getDescription())
                .build();
    }

    @Override
    public Note read(Long primaryKey) {
        return null;
    }

    @Override
    public Note update(Note updateObject) {
        return null;
    }

    @Override
    public void delete(Long key) {

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

