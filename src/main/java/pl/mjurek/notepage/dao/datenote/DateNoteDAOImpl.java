package pl.mjurek.notepage.dao.datenote;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DateNoteDAOImpl implements DateNoteDAO {
    private static final String CREATE =
            "INSERT INTO date(date_stick_note,date_deadline_note) VALUES(:date_stick_note,:date_deadline_note);";
    private static final String READ =
            "SELECT date_id,date_stick_note,date_deadline_note,date_user_made_task from date WHERE date_id=:date_id;";
    private static final String UPDATE =
            "UPDATE date SET date_deadline_note=:deadline, date_user_made_task=:date_user_made_task " +
                    "WHERE date_id=:date_id;";
    private static final String DELETE =
            "DELETE FROM date WHERE date_id=:date_id;";

    private NamedParameterJdbcTemplate template;

    public DateNoteDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }

    @Override
    public DateNote create(DateNote dateNote) throws AddObjectException {
        DateNote copyDate = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("date_stick_note", dateNote.getDateStickNote());
        paramMap.put("date_deadline_note", dateNote.getDateDeadlineNote());

        SqlParameterSource parameterSource = new MapSqlParameterSource(paramMap);
        int update = template.update(CREATE, parameterSource, keyHolder);
        if (update > 0) {
            copyDate = copyAndUpdate(dateNote, keyHolder);
        } else {
            throw new AddObjectException();
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
    public DateNote update(DateNote updateDate) {
        DateNote result = updateDate.toBuilder().build();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("date_deadline_note", updateDate.getDateDeadlineNote());
        paramMap.put("date_user_made_task", updateDate.getDateUserMadeTask());

        SqlParameterSource paramSource =new MapSqlParameterSource(paramMap);
        int update = template.update(UPDATE, paramSource, keyHolder);
        if (update > 0) {
            result.setId(keyHolder.getKey().longValue());
        }
        return result;
    }

    @Override
    public void delete(Long key) throws DeleteObjectException {
        MapSqlParameterSource paramSource = new MapSqlParameterSource("date_id", key);

        int update = template.update(DELETE, paramSource);
        if (update < 1) {
            throw new DeleteObjectException();
        }
    }

    private class DateNoteRowMapper implements RowMapper<DateNote> {
        @Override
        public DateNote mapRow(ResultSet resultSet, int i) throws SQLException {
            return DateNote.builder()
                    .id(resultSet.getLong("date_id"))
                    .dateStickNote(resultSet.getTimestamp("date_stick_note"))
                    .dateDeadlineNote(resultSet.getTimestamp("date_deadline_note"))
                    .dateUserMadeTask(resultSet.getTimestamp("date_user_made_task"))
                    .build();
        }
    }
}
