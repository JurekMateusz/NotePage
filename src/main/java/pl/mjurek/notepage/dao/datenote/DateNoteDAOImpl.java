package pl.mjurek.notepage.dao.datenote;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import pl.mjurek.notepage.exception.CantAddObjectException;
import pl.mjurek.notepage.model.DateNote;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.util.ConnectionProvider;

import java.util.HashMap;
import java.util.Map;

public class DateNoteDAOImpl implements DateNoteDAO {
    private static final String CREATE_DATE =
            "INSERT INTO date(date_stick_note,date_deadline_note) VALUES(:date_stick_note,:date_deadline_note);";

    private NamedParameterJdbcTemplate template;

    public DateNoteDAOImpl() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDataSource());
    }

    @Override
    public DateNote create(DateNote dateNote) throws CantAddObjectException {
        DateNote copyDate = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("date_stick_note", dateNote.getDateStickNote());
        paramMap.put("date_deadline_note", dateNote.getDateDeadlineNote());

        SqlParameterSource parameterSource = new MapSqlParameterSource(paramMap);
        int update = template.update(CREATE_DATE, parameterSource, keyHolder);
        if (update > 0) {
            copyDate = copyAndUpdate(dateNote, keyHolder);
        } else {
            throw new CantAddObjectException();
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
    public DateNote read(Long primaryKey) {
        return null;
    }

    @Override
    public DateNote update(DateNote updateObject) {
        return null;
    }

    @Override
    public void delete(Long key) {

    }
}
