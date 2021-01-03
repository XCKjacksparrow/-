package cn.bestrivenlf.electronadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * author : kazemi
 * date : 2020/09/06
 */
@Entity
@Table(name = "t_signin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ip;

    @Column
    private String userName;

    @Column
    private Long gmtCreate;

    @Column
    private Long gmtModified;

}
