package com.finpilot.finpilotai.service;

import com.finpilot.finpilotai.dto.IncomeRequest;
import com.finpilot.finpilotai.entity.Income;
import com.finpilot.finpilotai.entity.User;
import com.finpilot.finpilotai.repository.IncomeRepository;
import com.finpilot.finpilotai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(
            IncomeRepository incomeRepository,
            UserRepository userRepository) {

        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public String addIncome(IncomeRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Income income = new Income();

        income.setSource(request.getSource());
        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());

        income.setUser(user.get());

        incomeRepository.save(income);

        return "Income Added Successfully";
    }

    public List<Income> getMyIncome() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return List.of();
        }

        return incomeRepository.findByUser(user.get());
    }

    public String updateIncome(
            Long id,
            IncomeRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Income> income =
                incomeRepository.findById(id);

        if (income.isEmpty()) {
            return "Income Not Found";
        }

        Income existingIncome = income.get();

        if (existingIncome.getUser() == null) {
            return "Income Owner Not Found";
        }

        if (existingIncome.getUser().getId()
                != user.get().getId()) {

            return "Unauthorized Access";
        }

        existingIncome.setSource(
                request.getSource());

        existingIncome.setAmount(
                request.getAmount());

        existingIncome.setDescription(
                request.getDescription());

        incomeRepository.save(existingIncome);

        return "Income Updated Successfully";
    }

    public String deleteIncome(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Optional<User> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User Not Found";
        }

        Optional<Income> income =
                incomeRepository.findById(id);

        if (income.isEmpty()) {
            return "Income Not Found";
        }

        Income existingIncome = income.get();

        if (existingIncome.getUser() == null) {
            return "Income Owner Not Found";
        }

        if (existingIncome.getUser().getId()
                != user.get().getId()) {

            return "Unauthorized Access";
        }

        incomeRepository.deleteById(id);

        return "Income Deleted Successfully";
    }
}