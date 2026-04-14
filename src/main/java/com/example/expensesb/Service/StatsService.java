package com.example.expensesb.Service;

import com.example.expensesb.DTO.Stats.DonutChartRes;
import com.example.expensesb.DTO.Stats.HistoChartRes;
import com.example.expensesb.Entity.MyUser;
import com.example.expensesb.Enum.Periods;
import com.example.expensesb.Repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class StatsService {

    private final CategoryRepo categoryRepo;
    private final ExpenseRepo expenseRepo;
    private final IncomeRepo incomeRepo;
    private final IncomeSourceRepo incomeSourceRepo;
    private final MyUserRepo myUserRepo;
    private final EntityManager em;

    public StatsService(CategoryRepo categoryRepo,
                        ExpenseRepo expenseRepo,
                        MyUserRepo myUserRepo,
                        IncomeRepo incomeRepo,
                        IncomeSourceRepo incomeSourceRepo,
                        EntityManager em) {

        this.categoryRepo = categoryRepo;
        this.expenseRepo = expenseRepo;
        this.myUserRepo = myUserRepo;
        this.incomeRepo = incomeRepo;
        this.incomeSourceRepo = incomeSourceRepo;
        this.em = em;
    }

    public List<DonutChartRes> getExpenseByCategory(LocalDate start, LocalDate end) {


        MyUser user = getMyUser();


        return expenseRepo.findByDonutChartData(user,start,end);

    }

    public List<DonutChartRes> getIncomeBySource(LocalDate start, LocalDate end) {

        MyUser user = getMyUser();

        return incomeRepo.findByDonutChartData(user,start,end);
    }

    public List<HistoChartRes> getHistogrameData(LocalDate start, LocalDate end, Periods period) {

        MyUser user = getMyUser();
        String trunc = mapToSqlPeriod(period);

        // Expense query
        String expenseSql = """
        SELECT DATE_TRUNC('%s', e.date) AS period,
               SUM(e.cost) AS total
        FROM expense e
        WHERE e.user_id = :user
          AND e.date >= :start
          AND e.date < :end
        GROUP BY DATE_TRUNC('%s', e.date)
        ORDER BY period
    """.formatted(trunc, trunc);

        // Income query
        String incomeSql = """
        SELECT DATE_TRUNC('%s', i.date) AS period,
               SUM(i.amount) AS total
        FROM income i
        WHERE i.user_id = :user
          AND i.date >= :start
          AND i.date < :end
        GROUP BY DATE_TRUNC('%s', i.date)
        ORDER BY period
    """.formatted(trunc, trunc);

        Query expenseQuery = em.createNativeQuery(expenseSql);
        expenseQuery.setParameter("user", user.getId());
        expenseQuery.setParameter("start", start);
        expenseQuery.setParameter("end", end);

        Query incomeQuery = em.createNativeQuery(incomeSql);
        incomeQuery.setParameter("user", user.getId());
        incomeQuery.setParameter("start", start);
        incomeQuery.setParameter("end", end);

        List<Object[]> expenseRows = expenseQuery.getResultList();
        List<Object[]> incomeRows = incomeQuery.getResultList();

        //  format date from sql to localdate
        Map<LocalDate, Double> expenseMap = formatDate(expenseRows);
        Map<LocalDate, Double> incomeMap = formatDate(incomeRows);

        //  Step 2: Merge keys
        Set<LocalDate> allPeriods = new TreeSet<>();
        allPeriods.addAll(expenseMap.keySet());
        allPeriods.addAll(incomeMap.keySet());

        //  Step 3: Build result
        List<HistoChartRes> result = new ArrayList<>();

        for (LocalDate periodDate : allPeriods) {
            HistoChartRes dto = new HistoChartRes();

            dto.setPeriod(periodDate.toString()); // you can format later
            dto.setTotalExpenses(expenseMap.getOrDefault(periodDate, 0.0));
            dto.setTotalIncome(incomeMap.getOrDefault(periodDate, 0.0));

            result.add(dto);
        }

        return result;
    }

    private Map<LocalDate, Double> formatDate(List<Object[]> incomeRows) {
        Map<LocalDate, Double> map = new HashMap<>();
        for (Object[] row : incomeRows) {
            Object periodObj = row[0];
            LocalDate periodDate;

            if (periodObj instanceof java.sql.Timestamp ts) {
                periodDate = ts.toLocalDateTime().toLocalDate();
            }
            else if (periodObj instanceof java.time.Instant instant) {
                periodDate = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            }
            else if (periodObj instanceof java.time.LocalDateTime ldt) {
                periodDate = ldt.toLocalDate();
            }
            else {
                throw new RuntimeException("Unsupported date type: " + periodObj.getClass());
            }            Double total = ((Number) row[1]).doubleValue();
            map.put(periodDate, total);
        }

        return map;

    }

    public MyUser getMyUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return myUserRepo.findByUsername(username).orElseThrow(()->new EntityNotFoundException("username not found"));
    }
    public String mapToSqlPeriod(Periods period){
        return switch(period){
            case WEEK -> "week";
            case MONTH -> "month";
            case YEAR -> "year";
            case DAY -> "day";
        };
    }
}
