using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NUnit.Framework;
using tt = Microsoft.VisualStudio.TestTools.UnitTesting;
using UI.Model;
using System.Windows.Media.Imaging;

namespace UI.UnitTest
{
    [TestFixture]
    public class RepositoryTest
    {

        Repository rep;

        [SetUp]
        public void Setup()
        {
            this.rep = Repository.instance;
            
        }

        [TearDown]
        public void TearDown()
        {

        }

        /**
         * Test if Bitmaps are correctly assigned to strings) */
        [Test]
        public void TestBitmaps()
        {
            //for (int i = 0; i < this.rep.ImagePathObjects.Count;i+=1 )
            //{
            //    BitmapImage _bi = new BitmapImage();
            //    _bi.BeginInit();
            //    _bi.UriSource = new Uri(this.rep.ImagePathObjects.ElementAt(i), UriKind.Absolute);
            //    _bi.EndInit();
            //    NUnit.Framework.Assert.True(_bi == this.rep.getBitmapImage(i));
            //}
        }

    }
}
