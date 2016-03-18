package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Aluno;
import model.Grade;
import model.Historico;
import controller.util.ConnectionFactory;

public class ImportaDAOImpl {





	public void gravarRegistros(List<Grade> gradesGravada) throws SQLException{
		Connection con = null;





		try {
			con = new ConnectionFactory().getConnection();
			con.setAutoCommit(false);
			long idgrade = 0;
			long idaluno = 0;
			long idhistorico = 0;
			PreparedStatement stmt;
			ResultSet rs;
			String sql;


			stmt = con.prepareStatement("select max(id) max from grade");
			rs = stmt.executeQuery();
			while (rs.next()) {	idgrade = rs.getLong("max");}

			stmt = con.prepareStatement("select max(id) max from aluno");
			rs = stmt.executeQuery();
			while (rs.next()) {	idaluno = rs.getLong("max");}

			stmt = con.prepareStatement("select max(id) max from historico");
			rs = stmt.executeQuery();
			while (rs.next()) {	idhistorico = rs.getLong("max");
			}

			idgrade++;
			idaluno++;
			idhistorico++;

			for (Grade gradeQuestao : gradesGravada){
				long idGradeExistente = idgrade;
				if (gradeQuestao.getId() == null || gradeQuestao.getId() == 0){

					sql = "INSERT INTO grade("
							+ "ID,"
							+ "CODIGO,"
							+ "HORAS_ACE,"
							+ "HORAS_ELETIVAS,"
							+ "HORAS_OPCIONAIS,"
							+ "PERIODOS,"
							+ "PERIODO_INICIO,"
							+ "ID_CURSO) "
							+ "VALUES(?,?,?,?,?,?,?,?)";

					stmt = con.prepareStatement(sql);

					stmt.setLong(1, idgrade);
					stmt.setString(2, gradeQuestao.getCodigo());
					stmt.setInt(3, 0);
					stmt.setInt(4, 0);
					stmt.setInt(5, 0);
					stmt.setInt(6, 16);
					stmt.setInt(7, 1);
					stmt.setLong(8, gradeQuestao.getCurso().getId());
					stmt.execute();

				}	

				else {

					idGradeExistente = gradeQuestao.getId();


				}



				for (Aluno alunoQuestao : gradeQuestao.getGrupoAlunos()){

					sql = "INSERT INTO aluno("
							+ "id,"
							+ "MATRICULA,"
							+ "NOME,"
							+ "ID_CURSO,"
							+ "ID_GRADE) "
							+ "VALUES(?,?,?,?,?)";

					stmt = con.prepareStatement(sql);
					stmt.setLong(1, idaluno);
					stmt.setString(2, alunoQuestao.getMatricula());
					stmt.setString(3, alunoQuestao.getNome());
					stmt.setLong(4, gradeQuestao.getCurso().getId());
					stmt.setLong(5, idGradeExistente);
					stmt.execute();


					for (Historico historicoQuestao : alunoQuestao.getGrupoHistorico()){

						sql = "INSERT INTO historico("
								+ "id,"
								+ "NOTA,"
								+ "SEMESTRE_CURSADO,"
								+ "STATUS_DISCIPLINA,"
								+ "ID_MATRICULA,"
								+ "ID_DISCIPLINA) "
								+ "VALUES(?,?,?,?,?,?)";

						stmt = con.prepareStatement(sql);
						stmt.setLong(1, idhistorico);
						stmt.setString(2, historicoQuestao.getNota());
						stmt.setString(3, historicoQuestao.getSemestreCursado());
						stmt.setString(4, historicoQuestao.getStatusDisciplina());
						stmt.setLong(5, idaluno);
						stmt.setLong(6, historicoQuestao.getDisciplina().getId());
						stmt.execute();	

						idhistorico++;
					}

					idaluno++;
				}
				idgrade++;	
			}

			stmt.close();
			con.commit();



		} catch (Exception e) {
			// TODO: handle exception

		}
		finally {
			
			con.close();
		}

	}
}