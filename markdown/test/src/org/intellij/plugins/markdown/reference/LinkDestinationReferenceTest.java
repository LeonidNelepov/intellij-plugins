package org.intellij.plugins.markdown.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.FakePsiElement;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.jetbrains.annotations.NotNull;

public class LinkDestinationReferenceTest extends LightPlatformCodeInsightFixtureTestCase {

  public static final Logger LOGGER = Logger.getInstance(LinkDestinationReferenceTest.class);

  @NotNull
  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/reference/linkDestination/";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.configureByFile("sample.md");
    myFixture.copyDirectoryToProject("app", "app");
  }

  private void doTest() {
    final PsiFile file = myFixture.getFile();
    final String fileText = file.getText();

    final int linkTitle = fileText.indexOf("[" + getTestName(true) + "]");
    assertTrue(linkTitle >= 0);

    final int app = fileText.indexOf("app", linkTitle);
    assertTrue(app >= 0);
    assertGoodReference(file, app);

    final int foo = fileText.indexOf("foo", app);
    assertTrue(foo >= 0);
    assertGoodReference(file, foo);
  }

  private static void assertGoodReference(PsiFile file, int app) {
    final PsiReference reference = file.findReferenceAt(app);
    assertNotNull(reference);
    if (reference instanceof PsiPolyVariantReference) {
      assertTrue(((PsiPolyVariantReference)reference).multiResolve(false).length > 0);
    }
    else {
      assertNotNull(reference.resolve());
    }

    if (LOGGER.isDebugEnabled()) {
      LOGGER.info(String.valueOf(reference.resolve().getClass()));
    }
  }

  public void testRef() {
    doTest();
  }

  public void testRefBrack() {
    doTest();
  }

  public void testLink() {
    doTest();
  }

  public void testLinkBrack() {
    doTest();
  }

  public void testTrailingSlashUrl() {
    final PsiFile file = myFixture.configureByFile( "trailingSlashUrl.md");
    final String fileText = file.getText();

    final PsiReference reference = file.findReferenceAt(fileText.indexOf("app", fileText.indexOf("[url]")));
    assertNotNull(reference);

    assertTrue((reference.resolve()) instanceof FakePsiElement);
  }
}
