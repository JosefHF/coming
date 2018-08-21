package fr.inria.coming.core.filter.files;

import java.util.List;

import fr.inria.coming.core.Parameters;
import fr.inria.coming.core.filter.AbstractChainedFilter;
import fr.inria.coming.core.interfaces.Commit;
import fr.inria.coming.core.interfaces.FileCommit;
import fr.inria.coming.core.interfaces.IFilter;

public class CommitSizeFilter extends AbstractChainedFilter {

	
	public CommitSizeFilter() {
		super();
	
	}

	public CommitSizeFilter(IFilter parentFilter) {
		super(parentFilter);
		
	}

	@Override
	public boolean acceptCommit(Commit commit) {

		if (super.acceptCommit(commit)) {

			// Retrieve a list of file affected by the commit
			List<FileCommit> javaFiles = commit.getJavaFileCommits();
			int countJava = 0, nTests = 0;

			for (FileCommit fileCommit : javaFiles) {
				if (!fileCommit.getCompletePath().toLowerCase().endsWith("package-info.java"))
					countJava++;
				if (fileCommit.getCompletePath().toLowerCase().contains("test"))
					nTests++;

			}

			if (countJava > Parameters.MAX_FILES_PER_COMMIT) {
				// System.out.println("Commit not accepted, many files in the commit");
				// log.info("-----");
				return false;
			}
			
			if (Parameters.ONLY_COMMIT_WITH_TEST_CASE && nTests == 0){
				return false; 
			}
			//Finally, we accept the commit.
			return true;
		}
		return false;
	}

}
