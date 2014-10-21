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
    public class TileTest
    {

        Tile testTile;

        /**
        * instantiate a tile for the tests*/
        [SetUp]
        public void Setup()
        {
            Repository rep = Repository.instance;
            this.testTile = new Tile(rep.getTileImage(0), 10, 20);
        }

        [TearDown]
        public void TearDown()
        {

        }

        /**
        * Test if instantiation worked and correct parameters were handed in*/
        [Test]
        public void TestInstantiation()
        {
            NUnit.Framework.Assert.True(new BitmapImage().GetType() == this.testTile.BmImage.GetType());
            NUnit.Framework.Assert.True(this.testTile.X == 10);
            NUnit.Framework.Assert.True(this.testTile.Y == 20);
        }

        /**
         * Test if Rotation value works correctly with floats*/
        [Test]
        public void TestIncrementRotation()
        {
            for (int i = 0; i < 2000; i += 1)
            {
                this.testTile.Rotation += 0.1f;
                NUnit.Framework.Assert.True(this.testTile.Rotation == (i / (i * 10f)));
            }
        }

    }
}
