package cn.bestrivenlf.electronadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_chateach")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chateach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ip1;

    @Column
    private String userName1;

    @Column
    private String ip2;

    @Column
    private String userName2;

    @Column
    private String fromIdentity1;

    @Column
    private String fromIdentity2;


    @Column
    private String content;

    @Column
    private Long gmtCreate;

    @Column
    private Long gmtModified;

}
