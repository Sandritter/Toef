using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;
using UI.Enums;

namespace UI.Model
{
    /// <summary>
    /// enum containing the categories shown in the menu
    /// </summary>
    public enum MenuCategories
    {
        Landscapes,
        Streets,
        Participants
    }

    /// <summary>
    /// Creating the menu objects
    /// </summary>
    public class Menu
    {
        Repository repo;

        // list of all icons in the menu
        List<MenuIcon> icons;
        public List<MenuIcon> Icons
        {
            get
            {
                return icons;
            }
        }

        /// <summary>
        /// instantiating lists for menu categories
        /// </summary>
        public Menu()
        {
            // init repo
            repo = Repository.instance;

            this.icons = new List<MenuIcon>();

            // add landscape tiles
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.Soil), "tooltip", MenuCategories.Landscapes, new Tile(0, 0, 0, TileType.Soil)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.Grass), "tooltip", MenuCategories.Landscapes, new Tile(0, 0, 0, TileType.Grass)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.Forest1), "tooltip", MenuCategories.Landscapes, new Tile(0, 0, 0, TileType.Forest1)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.Forest2), "tooltip", MenuCategories.Landscapes, new Tile(0, 0, 0, TileType.Forest2)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.House), "tooltip", MenuCategories.Landscapes, new Tile(0, 0, 0, TileType.House)));

            // add street tiles
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.StreetStraight), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.StreetStraight)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.StreetCurve), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.StreetCurve)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.StreetCrossing), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.StreetCrossing)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.StreetT), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.StreetT)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.StreetZebra), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.StreetZebra)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.StreetBridge), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.StreetBridge)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.WaterStraight), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.WaterStraight)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.WaterCurve), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.WaterCurve)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.WaterCrossing), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.WaterCrossing)));
            this.icons.Add(new MenuIcon(repo.getTileImage(TileType.WaterT), "tooltip", MenuCategories.Streets, new Tile(0, 0, 0, TileType.WaterT)));

            // add participants
            //int imageOffset = repo.TileImages.Count - repo.ParticipantImages.Count + 1;
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.CabrioPink), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.CabrioPink)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.CabrioBlue), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.CabrioBlue)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.CabrioGreen), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.CabrioGreen)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.CabrioSilver), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.CabrioSilver)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.CabrioYellow), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.CabrioYellow)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.LimoBlue), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.LimoBlue)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.Kanu), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.Kanu)));
            this.icons.Add(new MenuIcon(repo.getParticipantImage(ParticipantType.Boat), "Hulk Hogen", MenuCategories.Participants, new Participant(ParticipantType.Boat)));


        }
    }
}
