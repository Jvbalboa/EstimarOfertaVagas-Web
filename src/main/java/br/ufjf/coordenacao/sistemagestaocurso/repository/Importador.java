package br.ufjf.coordenacao.sistemagestaocurso.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.ufjf.coordenacao.sistemagestaocurso.model.Aluno;
import br.ufjf.coordenacao.sistemagestaocurso.model.Grade;
import br.ufjf.coordenacao.sistemagestaocurso.model.Historico;

public class Importador {

	/*public Connection getConnection() {
	     try {
	         return DriverManager.getConnection("jdbc:mysql://localhost/sgc2", "sgc2", "sgc2");
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	 }
	*/
	@Inject
	private EntityManager manager;
	
	public void gravarRegistros(List<Grade> grades)
	{
		long idgrade = (long) manager.createNativeQuery("select max(id) max from grade").getSingleResult();
		long idaluno = (long) manager.createNativeQuery("select max(id) max from aluno").getSingleResult();
		long idhistorico = (long) manager.createNativeQuery("select max(id) max from historico").getSingleResult();
		
		idgrade++;
		idaluno++;
		idhistorico++;
		
		for (Grade gradeQuestao : grades){
			long idGradeExistente = idgrade;
			if (gradeQuestao.getId() == null || gradeQuestao.getId() == 0){

				manager.createNativeQuery("INSERT INTO grade("
						+ "ID,"
						+ "CODIGO,"
						+ "HORAS_ACE,"
						+ "HORAS_ELETIVAS,"
						+ "HORAS_OPCIONAIS,"
						+ "PERIODOS,"
						+ "PERIODO_INICIO,"
						+ "ID_CURSO) "
						+ "VALUES(?,?,?,?,?,?,?,?)")
				.setParameter(1, idgrade)
				.setParameter(2, gradeQuestao.getCodigo())
				.setParameter(3, 0)
				.setParameter(4, 0)
				.setParameter(5, 0)
				.setParameter(6, 16)
				.setParameter(7, 1)
				.setParameter(8, gradeQuestao.getCurso().getId())
				.executeUpdate();

			}	
			else {
				idGradeExistente = gradeQuestao.getId();
			}
			for (Aluno alunoQuestao : gradeQuestao.getGrupoAlunos()){

				manager.createNativeQuery("INSERT INTO aluno("
						+ "id,"
						+ "MATRICULA,"
						+ "NOME,"
						+ "ID_CURSO,"
						+ "ID_GRADE) "
						+ "VALUES(?,?,?,?,?)")

				.setParameter(1, idaluno)
				.setParameter(2, alunoQuestao.getMatricula())
				.setParameter(3, alunoQuestao.getNome())
				.setParameter(4, gradeQuestao.getCurso().getId())
				.setParameter(5, idGradeExistente)
				.executeUpdate();
				
				for (Historico historicoQuestao : alunoQuestao.getGrupoHistorico()){

					manager.createNativeQuery("INSERT INTO historico("
							+ "id,"
							+ "NOTA,"
							+ "SEMESTRE_CURSADO,"
							+ "STATUS_DISCIPLINA,"
							+ "ID_MATRICULA,"
							+ "ID_DISCIPLINA) "
							+ "VALUES(?,?,?,?,?,?)")

					.setParameter(1, idhistorico)
					.setParameter(2, historicoQuestao.getNota())
					.setParameter(3, historicoQuestao.getSemestreCursado())
					.setParameter(4, historicoQuestao.getStatusDisciplina())
					.setParameter(5, idaluno)
					.setParameter(6, historicoQuestao.getDisciplina().getId())
					
					.executeUpdate();

					idhistorico++;
				}

				idaluno++;
			}
			idgrade++;
		}
			
	}
	
	public void gravarRegistros2(List<Grade> gradesGravada) throws SQLException{
		/*Connection con = null;
		
		try {
			con = getConnection();
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
	*/
	}
}
