package web.api.services;


import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.api.models.Account;
import web.api.models.Transaction;
import web.api.models.User;
import web.api.repositories.AccountRepository;
import web.api.repositories.TransactionRepository;

import java.util.List;

@Service
public class TransactionService
{
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository)
    {
        this.transactionRepository=transactionRepository;
        this.accountRepository = accountRepository;
    }


    public int addTransaction(Transaction transaction)
    {

        try {
            transactionRepository.save(transaction);
            System.out.println("Transaction succeeded");
            return 1;
        }  catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }


    public List<Transaction> getTransaction(Account account)
     {
        List<Transaction> allTransactions = transactionRepository.findTransactionsByAccountEquals(account);
        return allTransactions;
    }

    public @Nullable Long getLatestTransactionId(){
        Long l;
       Transaction latestTran = transactionRepository.findTopByOrderByTransactionId();
       if(latestTran==null){
           l = new Long(100000);
       }
       else{
           l = latestTran.getTransactionId();
       }
       return l;
    }

}
